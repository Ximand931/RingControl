package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.bluetooth.BluetoothDevice
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.command.ReplaceTimetableCommand
import com.happs.ximand.ringcontrol.model.`object`.command.simple.WeekendMode
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.bl.BluetoothCommunicator
import com.happs.ximand.ringcontrol.model.bl.callback.ConnectCallback
import com.happs.ximand.ringcontrol.model.bl.callback.SendCallback
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository
import com.happs.ximand.ringcontrol.model.specification.impl.GetAllSqlSpecification
import com.happs.ximand.ringcontrol.view.fragment.AddTimetableFragment
import com.happs.ximand.ringcontrol.view.fragment.SettingsFragment
import com.happs.ximand.ringcontrol.view.fragment.TimetableInfoFragment.Companion.newInstance
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtils.getCurrentTimeWithFewMinutes
import java.util.*

class AllTimetablesViewModel : BaseViewModel(), SendCallback, ConnectCallback {

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
                SharedPreferencesDao.instance.getAppliedTimetableId()
        )
        manualModeStateLiveData.value = SharedPreferencesDao.instance.getManualModeState()
        weekendModeLiveData.value = getCurrentWeekendMode()
        updateData()
        startConnectingToSavedDevice()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BluetoothCommunicator.instance.sendCallback = this
        BluetoothCommunicator.instance.connectCallback = this
    }

    private fun getCurrentWeekendMode(): WeekendMode {
        return WeekendMode.getInstanceForModeId(SharedPreferencesDao.instance.getWeekendMode())
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

    override fun onConnected(device: BluetoothDevice) {
        applyingPossible.set(true)
    }

    override fun onSent(sentBytes: ByteArray) {
        if (sentBytes[0] == ReplaceTimetableCommand.CODE) {
            updateLastAppliedTimetableIfNecessary()
        }
    }

    override fun onException(e: BaseException) {
        applyingPossible.set(false)
    }

    private fun updateLastAppliedTimetableIfNecessary() {
        if (lastSentTimetableId != lastAppliedTimetableId.get()) {
            SharedPreferencesDao.instance
                    .updateAppliedTimetableId(lastSentTimetableId)
        }
        currentSendTimetableTask = false
    }

    override fun notifyOptionsMenuItemClicked(itemId: Int): Boolean {
        when (itemId) {
            R.id.toolbar_add -> {
                FragmentNavigation.instance.navigateTo(AddTimetableFragment.newInstance())
            }
            R.id.toolbar_settings -> {
                FragmentNavigation.instance.navigateTo(SettingsFragment.newInstance())
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
        val savedAddress = SharedPreferencesDao.instance.getTargetDeviceAddress()
        BluetoothCommunicator.instance
                .connectToDevice(checkNotNull(savedAddress))
    }

    fun showTimetableDetails(timetable: Timetable?) {
        val fragment = newInstance(timetable)
        FragmentNavigation.instance.navigateTo(fragment)
    }

    fun notifyAppliedTimetableUpdated() {
        val snackbarDto = SnackbarDto(
                R.string.applied_timetable_updated, Snackbar.LENGTH_SHORT
        ).setActionResId(R.string.apply)
                .setActionClickListener { applyUpdatedCurrentTimetable() }
        makeSnackbarEvent.value = snackbarDto
    }

    private fun applyUpdatedCurrentTimetable() {
        val appliedTimetableId = SharedPreferencesDao.instance.getAppliedTimetableId()
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
        BluetoothCommunicator.instance.writeBytes(command)
        currentSendTimetableTask = true
        lastSentTimetableId = timetable.id
    }

    companion object {
        private const val APPLIED_TIMETABLE_NONE = -2
    }

}