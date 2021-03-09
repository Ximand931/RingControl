package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.command.ReplaceTimetableCommand
import com.happs.ximand.ringcontrol.model.`object`.command.simple.WeekendMode
import com.happs.ximand.ringcontrol.model.`object`.info.BluetoothEvent
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository
import com.happs.ximand.ringcontrol.model.specification.impl.GetAllSqlSpecification
import com.happs.ximand.ringcontrol.view.fragment.AddTimetableFragment
import com.happs.ximand.ringcontrol.view.fragment.SettingsFragment
import com.happs.ximand.ringcontrol.view.fragment.TimetableInfoFragment.Companion.newInstance
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtils.getCurrentTimeWithFewMinutes
import me.aflak.bluetooth.Bluetooth.CommunicationCallback
import java.util.*

class AllTimetablesViewModel : BaseViewModel(), CommunicationCallback {

    val allTimetablesLiveData: MutableLiveData<MutableList<Timetable>> = MutableLiveData()
    val numOfTestLessonsLiveData = MutableLiveData<Int>()
    val applyingPossible = ObservableBoolean(false)
    val lastAppliedTimetableId: ObservableInt
    val manualModeStateLiveData = MutableLiveData<Boolean>()
    val weekendModeLiveData: MutableLiveData<WeekendMode> = MutableLiveData()

    private var currentSendTimetableTask = false
    private var lastSentTimetableId = APPLIED_TIMETABLE_NONE

    init {
        numOfTestLessonsLiveData.value = 2
        lastAppliedTimetableId = ObservableInt(
                SharedPreferencesDao.getInstance().getAppliedTimetableId()
        )
        manualModeStateLiveData.value = SharedPreferencesDao.getInstance().getManualModeState()
        weekendModeLiveData.value = getCurrentWeekendMode()
        updateData()
        startConnectingToSavedDevice()
    }

    private fun getCurrentWeekendMode(): WeekendMode {
        return WeekendMode.getInstanceForModeId(SharedPreferencesDao.getInstance().getWeekendMode())
    }

    fun isWeekendModeCaptionShouldBeShown(weekendMode: WeekendMode): Boolean {
        val weekday = getCurrentWeekday()
        return !(weekday != Calendar.SATURDAY && weekday != Calendar.SUNDAY
                || weekday != Calendar.SUNDAY && weekendMode == WeekendMode.MODE_WORK_ON_SATURDAY
                || weekendMode == WeekendMode.MODE_WORK_ON_WEEKENDS)
    }

    private fun getCurrentWeekday(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.DAY_OF_WEEK]
    }

    @Deprecated("")
    private fun onBluetoothEvent(event: BluetoothEvent) {
        if (event == BluetoothEvent.READY) {
            applyingPossible.set(true)
            updateLastAppliedTimetableIfNecessary()
        } else if (applyingPossible.get()) {
            applyingPossible.set(false)
        }
    }

    private fun updateLastAppliedTimetableIfNecessary() {
        if (currentSendTimetableTask && lastSentTimetableId != lastAppliedTimetableId.get()) {
            SharedPreferencesDao.getInstance()
                    .updateAppliedTimetableId(lastSentTimetableId)
        }
        currentSendTimetableTask = false
    }

    override fun notifyOptionsMenuItemClicked(itemId: Int): Boolean {
        when (itemId) {
            R.id.toolbar_add -> {
                FragmentNavigation.getInstance().navigateTo(AddTimetableFragment.newInstance())
            }
            R.id.toolbar_settings -> {
                FragmentNavigation.getInstance().navigateTo(SettingsFragment.newInstance())
            }
            else -> return false
        }
        return true
    }

    fun addTestLesson() {
        val numOfTestLessons = numOfTestLessonsLiveData.value!!
        numOfTestLessonsLiveData.value = numOfTestLessons + 1
    }

    fun removeTestLesson() {
        val numOfTestLessons = numOfTestLessonsLiveData.value!!
        numOfTestLessonsLiveData.value = numOfTestLessons - 1
    }

    fun updateData() {
        allTimetablesLiveData.value = TimetableRepository.getInstance()
                .query(GetAllSqlSpecification())
    }

    private fun startConnectingToSavedDevice() {
        val savedAddress = SharedPreferencesDao.getInstance().getTargetDeviceAddress()
        BluetoothNDao.getInstance().startConnecting(savedAddress, this)
    }

    fun showTimetableDetails(timetable: Timetable?) {
        val fragment = newInstance(timetable)
        FragmentNavigation.getInstance().navigateTo(fragment)
    }

    fun notifyAppliedTimetableUpdated() {
        val snackbarDto = SnackbarDto(
                R.string.applied_timetable_updated, Snackbar.LENGTH_SHORT
        ).setActionResId(R.string.apply)
                .setActionClickListener { applyUpdatedCurrentTimetable() }
        makeSnackbarEvent.value = snackbarDto
    }

    private fun applyUpdatedCurrentTimetable() {
        val appliedTimetableId = SharedPreferencesDao.getInstance().getAppliedTimetableId()
        val allTimetables: List<Timetable>? = allTimetablesLiveData.value
        if (allTimetables != null) {
            applyTimetable(allTimetables[appliedTimetableId])
        }
    }

    fun applyTestTimetable() {
        val numOfTestLessons = numOfTestLessonsLiveData.value!!
        val lessons: MutableList<Lesson> = ArrayList(numOfTestLessons * 2)
        var i = 1
        while (i <= numOfTestLessons * 2) {
            lessons.add(createLessonForTestTimetable(i))
            i += 2
        }
        applyTimetable(Timetable(-1, "Test", lessons))
    }

    private fun createLessonForTestTimetable(extraMinutes: Int): Lesson {
        val number = extraMinutes / 2 + 1
        return Lesson(number,
                getCurrentTimeWithFewMinutes(extraMinutes),
                getCurrentTimeWithFewMinutes(extraMinutes + 1)
        )
    }

    fun applyTimetable(timetable: Timetable) {
        val command: ByteArray = ReplaceTimetableCommand(timetable).toByteArray()
        BluetoothNDao.getInstance().sendMessage(command)
        currentSendTimetableTask = true
        lastSentTimetableId = timetable.id
    }

    override fun onConnect(device: BluetoothDevice) {
        Log.d("...", "connect")
        applyingPossible.set(true)
    }

    override fun onDisconnect(device: BluetoothDevice, message: String) {
        Log.d("...", "disconnect")
        applyingPossible.set(false)
    }

    override fun onMessage(message: String) {
        Log.d("...", "message: $message")
    }

    override fun onError(message: String) {
        Log.d("...", "error")
        applyingPossible.set(false)
    }

    override fun onConnectError(device: BluetoothDevice, message: String) {
        Log.d("...", "connect error")
        applyingPossible.set(false)
    }

    companion object {
        private const val APPLIED_TIMETABLE_NONE = -2

    }
}