package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.dao.BluetoothDao;
import com.happs.ximand.ringcontrol.model.dao.BluetoothEventListener;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.command.ReplaceTimetableCommand;
import com.happs.ximand.ringcontrol.model.object.command.simple.WeekendMode;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;
import com.happs.ximand.ringcontrol.model.object.timetable.Lesson;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;
import com.happs.ximand.ringcontrol.model.specification.impl.GetAllSqlSpecification;
import com.happs.ximand.ringcontrol.view.fragment.AddTimetableFragment;
import com.happs.ximand.ringcontrol.view.fragment.SettingsFragment;
import com.happs.ximand.ringcontrol.view.fragment.TimetableInfoFragment;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllTimetablesViewModel extends BaseViewModel {

    private static final int APPLIED_TIMETABLE_NONE = -2;

    private final MutableLiveData<List<Timetable>> allTimetablesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> numOfTestLessonsLiveData = new MutableLiveData<>();
    private final BluetoothEventListener<BluetoothEvent> bluetoothEventListener;
    private final ObservableBoolean applyingPossible = new ObservableBoolean(false);
    private final ObservableInt lastAppliedTimetableId;

    private final MutableLiveData<Boolean> manualModeStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<WeekendMode> weekendModeLiveData = new MutableLiveData<>();

    private boolean currentSendTimetableTask = false;
    private int lastSentTimetableId = APPLIED_TIMETABLE_NONE;

    public AllTimetablesViewModel() {
        this.numOfTestLessonsLiveData.setValue(2);
        this.bluetoothEventListener = new BluetoothEventListener<>(this::onBluetoothEvent);
        this.lastAppliedTimetableId = new ObservableInt(
                SharedPreferencesDao.getInstance().getAppliedTimetableId()
        );
        this.manualModeStateLiveData
                .setValue(SharedPreferencesDao.getInstance().getManualModeState());
        this.weekendModeLiveData.setValue(getCurrentWeekendMode());
        updateData();
        startConnectingToSavedDevice();
    }

    private WeekendMode getCurrentWeekendMode() {
        return WeekendMode.getInstanceForModeId(SharedPreferencesDao.getInstance().getWeekendMode());
    }

    public LiveData<List<Timetable>> getAllTimetablesLiveData() {
        return allTimetablesLiveData;
    }

    public LiveData<Integer> getNumOfTestLessonsLiveData() {
        return numOfTestLessonsLiveData;
    }

    public ObservableBoolean getApplyingPossible() {
        return applyingPossible;
    }

    public ObservableInt getLastAppliedTimetableId() {
        return lastAppliedTimetableId;
    }

    public LiveData<Boolean> getManualModeStateLiveData() {
        return manualModeStateLiveData;
    }

    public LiveData<WeekendMode> getWeekendModeLiveData() {
        return weekendModeLiveData;
    }

    public boolean isWeekendModeCaptionShouldBeShown(WeekendMode weekendMode) {
        int weekday = getCurrentWeekday();
        return !((weekday != Calendar.SATURDAY && weekday != Calendar.SUNDAY)
                || (weekday != Calendar.SUNDAY && weekendMode == WeekendMode.MODE_WORK_ON_SATURDAY)
                || (weekendMode == WeekendMode.MODE_WORK_ON_WEEKENDS));
    }

    private int getCurrentWeekday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private void onBluetoothEvent(BluetoothEvent event) {
        if (event == BluetoothEvent.READY) {
            applyingPossible.set(true);
            updateLastAppliedTimetableIfNecessary();
        } else if (applyingPossible.get()) {
            applyingPossible.set(false);
        }
    }

    private void updateLastAppliedTimetableIfNecessary() {
        if (currentSendTimetableTask && lastSentTimetableId != lastAppliedTimetableId.get()) {
            SharedPreferencesDao.getInstance()
                    .updateAppliedTimetableId(lastSentTimetableId);
        }
        currentSendTimetableTask = false;
    }

    @Override
    public boolean notifyOptionsMenuItemClicked(int itemId) {
        switch (itemId) {
            case R.id.toolbar_add:
                FragmentNavigation.getInstance().navigateTo(AddTimetableFragment.newInstance());
                return true;
            case R.id.toolbar_settings:
                FragmentNavigation.getInstance().navigateTo(SettingsFragment.newInstance());
                return true;
            default:
                return false;
        }
    }

    public void addTestLesson() {
        @SuppressWarnings("ConstantConditions")
        int numOfTestLessons = numOfTestLessonsLiveData.getValue();
        numOfTestLessonsLiveData.setValue(numOfTestLessons + 1);
    }

    public void removeTestLesson() {
        @SuppressWarnings("ConstantConditions")
        int numOfTestLessons = numOfTestLessonsLiveData.getValue();
        numOfTestLessonsLiveData.setValue(numOfTestLessons - 1);
    }

    public void updateData() {
        List<Timetable> timetables = TimetableRepository.getInstance()
                .query(new GetAllSqlSpecification());
        allTimetablesLiveData.setValue(timetables);
    }

    private void startConnectingToSavedDevice() {
        String savedAddress = SharedPreferencesDao.getInstance().getTargetDeviceAddress();
        BluetoothDao.getInstance().startConnectToDeviceTask(savedAddress);
    }

    public void showTimetableDetails(Timetable timetable) {
        TimetableInfoFragment fragment = TimetableInfoFragment.newInstance(timetable);
        FragmentNavigation.getInstance().navigateTo(fragment);
    }

    public void notifyAppliedTimetableUpdated() {
        SnackbarDto snackbarDto = new SnackbarDto(
                R.string.applied_timetable_updated, Snackbar.LENGTH_SHORT
        ).setActionResId(R.string.apply)
                .setActionClickListener(v -> applyUpdatedCurrentTimetable());
        getMakeSnackbarEvent().setValue(snackbarDto);
    }

    public void applyUpdatedCurrentTimetable() {
        int appliedTimetableId = SharedPreferencesDao.getInstance().getAppliedTimetableId();
        List<Timetable> allTimetables = allTimetablesLiveData.getValue();
        if (allTimetables != null) {
            applyTimetable(allTimetables.get(appliedTimetableId));
        }
    }

    public void applyTestTimetable() {
        @SuppressWarnings("ConstantConditions")
        int numOfTestLessons = numOfTestLessonsLiveData.getValue();
        List<Lesson> lessons = new ArrayList<>(numOfTestLessons * 2);
        for (int i = 1; i <= numOfTestLessons * 2; i += 2) {
            lessons.add(createLessonForTestTimetable(i));
        }
        applyTimetable(new Timetable(-1, "Test", lessons));
    }

    private Lesson createLessonForTestTimetable(int extraMinutes) {
        int number = extraMinutes / 2 + 1;
        return new Lesson(number,
                TimeUtils.getCurrentTimeWithFewMinutes(extraMinutes),
                TimeUtils.getCurrentTimeWithFewMinutes(extraMinutes + 1)
        );
    }

    public void applyTimetable(Timetable timetable) {
        byte[] command = new ReplaceTimetableCommand(timetable).getCommand();
        BluetoothDao.getInstance().sendMessage(command);
        currentSendTimetableTask = true;
        lastSentTimetableId = timetable.getId();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        BluetoothDao.getInstance().unsubscribeFromEvents(bluetoothEventListener);
    }
}
