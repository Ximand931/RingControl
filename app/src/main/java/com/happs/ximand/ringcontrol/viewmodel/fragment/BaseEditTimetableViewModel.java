package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.timetable.Lesson;
import com.happs.ximand.ringcontrol.model.object.timetable.Time;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;

import java.util.List;

public abstract class BaseEditTimetableViewModel extends BaseViewModel {

    protected final MutableLiveData<Integer> numOfLessons = new MutableLiveData<>();
    private final MutableLiveData<List<Lesson>> lessonsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> titleLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> titleErrorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> detailEditingLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Void> addLessonEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> removeLessonEvent = new SingleLiveEvent<>();
    private CorrectnessCheck correctnessCheck;

    public LiveData<Integer> getNumOfLessons() {
        return numOfLessons;
    }

    public LiveData<List<Lesson>> getLessonsLiveData() {
        return lessonsMutableLiveData;
    }

    public MutableLiveData<String> getTitleLiveData() {
        return titleLiveData;
    }

    public MutableLiveData<Boolean> getTitleErrorLiveData() {
        return titleErrorLiveData;
    }

    public MutableLiveData<Boolean> getDetailEditingLiveData() {
        return detailEditingLiveData;
    }

    public SingleLiveEvent<Void> getAddLessonEvent() {
        return addLessonEvent;
    }

    public SingleLiveEvent<Void> getRemoveLessonEvent() {
        return removeLessonEvent;
    }

    protected CorrectnessCheck getCorrectnessCheck() {
        return correctnessCheck;
    }

    public void setCorrectnessCheck(CorrectnessCheck correctnessCheck) {
        this.correctnessCheck = correctnessCheck;
    }

    public void addEmptyLesson() {
        addLessonEvent.call();
        //noinspection ConstantConditions
        numOfLessons.setValue(numOfLessons.getValue() + 1);
    }

    public void removeLastLesson() {
        removeLessonEvent.call();
        //noinspection ConstantConditions
        numOfLessons.setValue(numOfLessons.getValue() - 1);
    }

    @Override
    public boolean notifyOptionsMenuItemClicked(int itemId) {
        switch (itemId) {
            case R.id.toolbar_do_edit_action:
                completeEditAction();
                return true;
            case R.id.toolbar_cancel:
                onCancelClick();
                return true;
        }
        return false;
    }

    protected void completeEditAction() {
        boolean correct = checkTitle() && checkLessons();
        if (correct) {
            onCompleteEditAction();
            onEditActionCompleted();
            FragmentNavigation.getInstance().navigateToPreviousFragment();
        } else {
            getMakeSnackbarEvent().setValue(new SnackbarDto(
                    getErrorMessageResId(), Snackbar.LENGTH_SHORT
            ));
        }
    }

    protected abstract void onCompleteEditAction();

    protected void onEditActionCompleted() {
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(
                AllTimetablesFragment.TAG, AllTimetablesFragment.EVENT_TIMETABLE_LIST_UPDATED
        );
    }

    protected abstract int getErrorMessageResId();

    protected void setLessonList(List<Lesson> lessons) {
        lessonsMutableLiveData.setValue(lessons);
        detailEditingLiveData.setValue(getDetailEditingStatusForCurrentLessonsList(lessons));
    }

    private boolean getDetailEditingStatusForCurrentLessonsList(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            boolean simplifiable = canTimeBeConvertedToSimple(lesson.getStartTime())
                    && canTimeBeConvertedToSimple(lesson.getEndTime());
            if (!simplifiable) {
                return true;
            }
        }
        return false;
    }

    private boolean canTimeBeConvertedToSimple(Time time) {
        return time.getSeconds() == 0;
    }

    protected boolean checkTitle() {
        boolean correct = !TextUtils.isEmpty(titleLiveData.getValue());
        if (!correct) {
            titleErrorLiveData.setValue(true);
        }
        return correct;
    }

    protected boolean checkLessons() {
        return getCorrectnessCheck().isListCorrect();
    }

    public void onCancelClick() {
        FragmentNavigation.getInstance().navigateToPreviousFragment();
    }

    public interface CorrectnessCheck {
        boolean isListCorrect();
    }
}
