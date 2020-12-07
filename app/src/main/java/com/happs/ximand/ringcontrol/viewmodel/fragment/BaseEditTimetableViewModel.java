package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.object.exception.IncorrectInputException;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.item.EditItemViewModel;

import java.util.ArrayList;
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
