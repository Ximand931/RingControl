package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.Lesson;

import java.util.List;

public abstract class BaseEditTimetableViewModel extends BaseViewModel {

    protected final MutableLiveData<Integer> numOfLessons = new MutableLiveData<>();
    private final MutableLiveData<List<Lesson>> lessonsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> titleLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> titleErrorLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Void> addLessonEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> removeLessonEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> changeDetailEditMode = new SingleLiveEvent<>();
    protected boolean detailEditing = true;
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

    public SingleLiveEvent<Void> getAddLessonEvent() {
        return addLessonEvent;
    }

    public SingleLiveEvent<Void> getRemoveLessonEvent() {
        return removeLessonEvent;
    }

    public SingleLiveEvent<Void> getChangeDetailEditMode() {
        return changeDetailEditMode;
    }

    protected CorrectnessCheck getCorrectnessCheck() {
        return correctnessCheck;
    }

    public void setCorrectnessCheck(CorrectnessCheck correctnessCheck) {
        this.correctnessCheck = correctnessCheck;
    }

    protected void setLessons(List<Lesson> lessons) {
        this.lessonsMutableLiveData.setValue(lessons);
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

    public void changeDetailEditMode() {
        changeDetailEditMode.call();
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

    protected abstract void completeEditAction();

    protected boolean checkTitle() {
        boolean correct = !TextUtils.isEmpty(titleLiveData.getValue());
        if (!correct) {
            titleErrorLiveData.setValue(true);
        }
        return correct;
    }

    protected boolean checkLessons() {
        boolean correct = getCorrectnessCheck().isListCorrect();
        if (!correct) {
            //TODO: on incorrect
        }
        return correct;
    }

    public void onCancelClick() {
        FragmentNavigation.getInstance().navigateToPreviousFragment();
    }

    public interface CorrectnessCheck {
        boolean isListCorrect();
    }
}
