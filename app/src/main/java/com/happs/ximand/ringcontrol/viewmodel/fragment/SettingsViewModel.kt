package com.happs.ximand.ringcontrol.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.command.SendDateCommand
import com.happs.ximand.ringcontrol.model.`object`.command.simple.*
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException
import com.happs.ximand.ringcontrol.model.`object`.exception.UnsuccessResponseException
import com.happs.ximand.ringcontrol.model.`object`.response.Response
import com.happs.ximand.ringcontrol.model.bl.BluetoothCommunicator
import com.happs.ximand.ringcontrol.model.bl.callback.ReceiveCallback
import com.happs.ximand.ringcontrol.model.bl.callback.SendCallback
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.viewmodel.dto.InputAlertDialogDto
import com.happs.ximand.ringcontrol.viewmodel.dto.SelectAlertDialogDto

@Suppress("RedundantLambdaArrow")
class SettingsViewModel : BaseViewModel(), SendCallback, ReceiveCallback {

    private val mapCodeSaveMethod =
            mapOf<Byte, (bytes: ByteArray) -> Unit>(
                    ChangeRingDurationCommand.CODE to { it -> saveNewRingDuration(it) },
                    ChangeWeekendModeCommand.CODE to { it -> saveNewWeekendMode(it) },
                    ChangeManualModeCommand.CODE to { it -> saveManualModeState(it) }
            )

    val inputIncorrectEvent = SingleLiveEvent<Int>()
    val dismissDialogEvent = SingleLiveEvent<Void>()
    val setRingDurationEvent = SingleLiveEvent<InputAlertDialogDto>()
    val setWeekendModeEvent = SingleLiveEvent<SelectAlertDialogDto>()
    val setManualRingDurationEvent = SingleLiveEvent<InputAlertDialogDto>()
    val sendDataPossible = MutableLiveData<Boolean>()
    val ringDurationLiveData = MutableLiveData<Int>()
    val weekendModeLiveData: MutableLiveData<WeekendMode> = MutableLiveData()
    val manualModeStateLiveData = MutableLiveData<Boolean>()

    private val communicator = BluetoothCommunicator.instance
    private val storageDao = SharedPreferencesDao.instance

    init {
        sendDataPossible.value = communicator.connected
        ringDurationLiveData.value = storageDao.getRingDuration()
        manualModeStateLiveData.value = storageDao.getManualModeState()
        weekendModeLiveData.value = getCurrentWeekendMode()
    }

    override fun onSent(sentBytes: ByteArray) {
        mapCodeSaveMethod[sentBytes[0]]?.invoke(sentBytes)
        makeSimpleSnackbar(R.string.success_sent)
    }

    override fun onReceive(bytes: ByteArray) {
        val response = Response.getResponseByMessage(bytes)
        if (!response.isSuccess) {
            makeExceptionSnackbarWithAction(R.string.unsuccessfully_response,
                    UnsuccessResponseException(response.responseCode.toInt()))
        }
    }

    override fun onException(e: BaseException) {
        makeExceptionSnackbarWithAction(R.string.bluetooth_error, e)
    }

    private fun getCurrentWeekendMode(): WeekendMode {
        val mode = SharedPreferencesDao.instance.getWeekendMode()
        return WeekendMode.getInstanceForModeId(mode)
    }

    fun updateRingDuration() {
        setRingDurationEvent.value = InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setInputCompleteListener { input: String ->
                    onSetDurationComplete(input) { sendNewRingDurationToDevice(it) }
                }
                .setCancelListener { makeSimpleSnackbar(R.string.changes_did_not_complet) }
    }

    private fun sendNewRingDurationToDevice(duration: Int) {
        val command = ChangeRingDurationCommand(duration.toByte())
        communicator.writeBytes(command.toByteArray())
    }

    private fun saveNewRingDuration(bytes: ByteArray) {
        ringDurationLiveData.value = bytes[1].toInt()
        SharedPreferencesDao.instance.updateRingDuration(bytes[1].toInt())
        makeSimpleSnackbar(R.string.success_sent)
    }

    fun updateWeekendMode() {
        setWeekendModeEvent.value = SelectAlertDialogDto()
                .setTitleResId(R.string.select_weekend_mode)
                .setItemsArrayResId(R.array.weekend_mods)
                .setSelectListener(this::onWeekendModeSelected)
                .setCancelListener { makeSimpleSnackbar(R.string.changes_did_not_complet) }
    }

    private fun onWeekendModeSelected(modeId: Int) {
        val command = ChangeWeekendModeCommand(modeId.toByte())
        communicator.writeBytes(command.toByteArray())
    }

    private fun saveNewWeekendMode(bytes: ByteArray) {
        weekendModeLiveData.value = WeekendMode.getInstanceForModeId(bytes[1].toInt())
        SharedPreferencesDao.instance.updateWeekendMode(bytes[1].toInt())
        makeSimpleSnackbar(R.string.success_sent)
    }

    fun updateManualModeState() {
        val newManualModeState = !manualModeStateLiveData.value!!
        val command = ChangeManualModeCommand(newManualModeState)
        communicator.writeBytes(command.toByteArray())
    }

    private fun saveManualModeState(bytes: ByteArray) {
        manualModeStateLiveData.value = convertByteToBoolean(bytes[1])
        SharedPreferencesDao.instance.updateManualMode(convertByteToBoolean(bytes[1]))
    }

    fun makeManualRing() {
        setManualRingDurationEvent.value = InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setInputCompleteListener { input ->
                    onSetDurationComplete(input) {
                        communicator.writeBytes(MakeRingCommand(it.toByte()).toByteArray())
                    }
                }
                .setCancelListener { makeSimpleSnackbar(R.string.changes_did_not_complet) }
    }

    private fun onSetDurationComplete(input: String, onComplete: (duration: Int) -> Unit) = try {
        val duration = input.toInt()
        if (duration == 0) {
            inputIncorrectEvent.value = R.string.zero_value
        } else {
            dismissDialogEvent.call()
            onComplete.invoke(duration)
        }
    } catch (e: NumberFormatException) {
        inputIncorrectEvent.value = R.string.wrong_number_format
    }

    fun sendNewDateTime() {
        communicator.writeBytes(SendDateCommand().toByteArray())
    }

    private fun convertByteToBoolean(b: Byte): Boolean {
        return b == 0xFF.toByte()
    }
}
