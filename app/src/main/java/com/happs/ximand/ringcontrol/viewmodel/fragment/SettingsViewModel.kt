package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.command.BluetoothCommand
import com.happs.ximand.ringcontrol.model.`object`.command.ChangeRingDurationCommandDep
import com.happs.ximand.ringcontrol.model.`object`.command.SendDateCommand
import com.happs.ximand.ringcontrol.model.`object`.command.simple.*
import com.happs.ximand.ringcontrol.model.`object`.exception.UnsuccessResponseException
import com.happs.ximand.ringcontrol.model.`object`.response.Response
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.viewmodel.dto.InputAlertDialogDto
import com.happs.ximand.ringcontrol.viewmodel.dto.SelectAlertDialogDto
import me.aflak.bluetooth.Bluetooth.CommunicationCallback
import java.util.*

class SettingsViewModel : BaseViewModel(), CommunicationCallback {

    val inputIncorrectEvent = SingleLiveEvent<Int>()
    val dismissDialogEvent = SingleLiveEvent<Void>()
    val setRingDurationEvent = SingleLiveEvent<InputAlertDialogDto>()
    val setWeekendModeEvent = SingleLiveEvent<SelectAlertDialogDto>()
    val setManualRingDurationEvent = SingleLiveEvent<InputAlertDialogDto>()
    val sendDataPossible = MutableLiveData<Boolean>()
    private val ringDurationLiveData = MutableLiveData<Int>()
    private val weekendModeLiveData: MutableLiveData<WeekendMode> = MutableLiveData()
    val manualModeStateLiveData = MutableLiveData<Boolean>()
    private var sentBluetoothCommand: BluetoothCommand<*>? = null

    init {
        sendDataPossible.value = BluetoothNDao.getInstance().isDeviceConnected
        ringDurationLiveData.value = SharedPreferencesDao.getInstance().getRingDuration()
        weekendModeLiveData.value = getCurrentWeekendMode()
        manualModeStateLiveData.value = SharedPreferencesDao.getInstance().getManualModeState()
    }

    private fun getCurrentWeekendMode(): WeekendMode {
        val mode = SharedPreferencesDao.getInstance().getWeekendMode()
        return WeekendMode.getInstanceForModeId(mode)
    }

    fun getDescriptionForWeekendMode(weekendMode: WeekendMode?): Int {
        return when (weekendMode) {
            WeekendMode.MODE_NOT_WORK_ON_WEEKENDS -> R.string.mode_not_work_on_weekends
            WeekendMode.MODE_WORK_ON_SATURDAY -> R.string.mode_work_on_saturday
            WeekendMode.MODE_WORK_ON_WEEKENDS -> R.string.mode_work_on_weekends
            else -> throw IllegalStateException()
        }
    }

    fun getRingDurationLiveData(): LiveData<Int> {
        return ringDurationLiveData
    }

    fun getWeekendModeLiveData(): LiveData<WeekendMode> {
        return weekendModeLiveData
    }

    private fun saveSentData(response: Response) {
        when (response.commandCode) {
            ChangeRingDurationCommandDep.COMMAND_CODE -> saveNewRingDuration()
            ChangeWeekendModeCommand.COMMAND_CODE -> saveNewWeekendMode()
            ChangeManualModeCommand.COMMAND_CODE -> saveManualModeState()
        }
    }

    fun updateRingDuration() {
        setRingDurationEvent.value = InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setInputCompleteListener { inputDuration: String -> onRingDurationFilled(inputDuration) }
                .setCancelListener { onChangeCanceled() }
    }

    private fun onRingDurationFilled(inputDuration: String) {
        try {
            val duration = inputDuration.toInt()
            if (duration < 3) {
                inputIncorrectEvent.setValue(R.string.too_small_value_n)
            } else if (duration > 59) {
                inputIncorrectEvent.setValue(R.string.too_big_value_n)
            } else {
                dismissDialogEvent.call()
                sendNewRingDurationToDevice(duration)
            }
        } catch (e: NumberFormatException) {
            inputIncorrectEvent.value = R.string.wrong_number_format
        }
    }

    private fun sendNewRingDurationToDevice(duration: Int) {
        val command = ChangeRingDurationCommand(duration.toByte())
        BluetoothNDao.getInstance().sendMessage(command.toByteArray())
        sentBluetoothCommand = command
        saveNewRingDuration()
    }

    private fun saveNewRingDuration() {
        val duration: Int = (sentBluetoothCommand as ChangeRingDurationCommand)
                .mainContent.toInt()
        ringDurationLiveData.value = duration
        SharedPreferencesDao.getInstance().updateRingDuration(duration)
        makeSimpleSnackbar(R.string.success_sent)
    }

    fun updateWeekendMode() {
        setWeekendModeEvent.value = SelectAlertDialogDto()
                .setTitleResId(R.string.select_weekend_mode)
                .setItemsArrayResId(R.array.weekend_mods)
                .setSelectListener(this::onWeekendModeSelected)
                .setCancelListener(this::onChangeCanceled)
    }

    private fun onWeekendModeSelected(modeId: Int) {
        val command = ChangeWeekendModeCommand(modeId.toByte())
        BluetoothNDao.getInstance().sendMessage(command.toByteArray())
        sentBluetoothCommand = command
        saveNewWeekendMode()
    }

    private fun saveNewWeekendMode() {
        val modeId: Int = (sentBluetoothCommand as ChangeWeekendModeCommand)
                .mainContent.toInt()
        weekendModeLiveData.value = WeekendMode.getInstanceForModeId(modeId)
        SharedPreferencesDao.getInstance().updateWeekendMode(modeId)
        makeSimpleSnackbar(R.string.success_sent)
    }

    fun updateManualModeState() {
        val newManualModeState = !manualModeStateLiveData.value!!
        val command = ChangeManualModeCommand(newManualModeState)
        BluetoothNDao.getInstance().sendMessage(command.toByteArray())
        sentBluetoothCommand = command
        saveManualModeState()
    }

    private fun saveManualModeState() {
        val newState = convertByteToBoolean((sentBluetoothCommand as ChangeManualModeCommand)
                .mainContent)
        manualModeStateLiveData.value = newState
        SharedPreferencesDao.getInstance().updateManualMode(newState)
        makeSimpleSnackbar(R.string.success_sent)
    }

    fun makeManualRing() {
        setManualRingDurationEvent.value = InputAlertDialogDto()
                .setTitleResId(R.string.ring_duration)
                .setInputCompleteListener(this::onManualRingDurationComplete)
                .setCancelListener(this::onChangeCanceled)
    }

    private fun onManualRingDurationComplete(input: String) {
        try {
            val duration = input.toInt()
            when {
                duration < 3 -> {
                    inputIncorrectEvent.setValue(R.string.too_small_value_n)
                }
                duration > 59 -> {
                    inputIncorrectEvent.setValue(R.string.too_big_value_n)
                }
                else -> {
                    dismissDialogEvent.call()
                    sendMakeRingCommand(duration)
                }
            }
        } catch (e: NumberFormatException) {
            inputIncorrectEvent.value = R.string.wrong_number_format
        }
    }

    private fun sendMakeRingCommand(d: Int) {
        BluetoothNDao.getInstance().sendMessage(MakeRingCommand(d.toByte()).toByteArray())
        makeSimpleSnackbar(R.string.success_sent)
    }

    fun sendNewDateTime() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH].toByte()
        val dateOfMonth = calendar[Calendar.DAY_OF_MONTH].toByte()
        val hours = calendar[Calendar.HOUR_OF_DAY].toByte()
        val minutes = calendar[Calendar.MINUTE].toByte()
        val seconds = calendar[Calendar.SECOND].toByte()
        val dow = calendar[Calendar.DAY_OF_WEEK].toByte()
        BluetoothNDao.getInstance().sendMessage(SendDateCommand(
                (year % 100).toByte(), month, dateOfMonth, hours, minutes, seconds, dow
        ).toByteArray())
        makeSimpleSnackbar(R.string.success_sent)
    }

    private fun onChangeCanceled() {
        makeSimpleSnackbar(R.string.changes_did_not_complet)
    }

    private fun convertByteToBoolean(b: Byte): Boolean {
        return b == 0xFF.toByte()
    }

    private fun convertByteToHexString(b: Byte): String {
        return Integer.toHexString(b.toInt())
    }

    override fun onConnect(device: BluetoothDevice) {
        Log.d("...", "connected")
        sendDataPossible.postValue(true)
    }

    override fun onDisconnect(device: BluetoothDevice, message: String) {
        Log.d("...", "disconnect")
        sendDataPossible.postValue(false)
    }

    override fun onMessage(message: String) {
        val response = Response.getResponseByMessage(message.toByteArray())
        if (response.isSuccess) {
            saveSentData(response)
        } else {
            makeExceptionSnackbarWithAction(R.string.unsuccessfully_response,
                    UnsuccessResponseException(response.responseCode.toInt()))
        }
    }

    override fun onError(message: String) {
        Log.d("...", "error")
        sendDataPossible.postValue(false)
    }

    override fun onConnectError(device: BluetoothDevice, message: String) {
        Log.d("...", "connectError")
        sendDataPossible.postValue(false)
    }
}