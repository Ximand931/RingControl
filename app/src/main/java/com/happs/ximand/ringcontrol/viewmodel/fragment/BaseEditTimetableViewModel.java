package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.model.object.Lesson;

import java.util.List;

public abstract class BaseEditTimetableViewModel extends BaseFragmentViewModel {

    private MutableLiveData<List<Lesson>> lessonsMutableLiveData = new MutableLiveData<>();
    protected boolean detailEditing;

    BaseEditTimetableViewModel(@NonNull Application application) {
        super(application);
        this.detailEditing = true;
    }

    public BaseEditTimetableViewModel() {
        this.detailEditing = true;
    }

    public MutableLiveData<List<Lesson>> getLessonsMutableLiveData() {
        return lessonsMutableLiveData;
    }

    public void addEmptyLesson() {
        //TODO
    }

    public void removeLastLesson() {
        //TODO
    }

    public void changeDetailEditMode() {
        //TODO
    }

}
