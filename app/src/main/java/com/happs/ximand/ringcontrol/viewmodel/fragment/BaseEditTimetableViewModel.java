package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.Lesson;

import java.util.List;

public abstract class BaseEditTimetableViewModel extends BaseFragmentViewModel {

    private final SingleLiveEvent<Void> addLessonEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> removeLessonEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> changeDetailEditMode = new SingleLiveEvent<>();
    private final MutableLiveData<List<Lesson>> lessonsMutableLiveData = new MutableLiveData<>();
    protected boolean detailEditing = true;

    public SingleLiveEvent<Void> getAddLessonEvent() {
        return addLessonEvent;
    }

    public SingleLiveEvent<Void> getRemoveLessonEvent() {
        return removeLessonEvent;
    }

    public SingleLiveEvent<Void> getChangeDetailEditMode() {
        return changeDetailEditMode;
    }

    public LiveData<List<Lesson>> getLessonsMutableLiveData() {
        return lessonsMutableLiveData;
    }

    protected void setLessons(List<Lesson> lessons) {
        this.lessonsMutableLiveData.setValue(lessons);
    }

    public void addEmptyLesson() {
        addLessonEvent.call();
    }

    public void removeLastLesson() {
        removeLessonEvent.call();
    }

    public void changeDetailEditMode() {
        changeDetailEditMode.call();
    }
}
