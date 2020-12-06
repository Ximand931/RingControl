package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.exception.IncorrectInputException;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.impl.FakeTimetableRepository;
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddTimetableViewModel extends BaseEditTimetableViewModel {

    private final SingleLiveEvent<Void> updateDataInAllTimetablesFragmentLiveEvent;

    private String title;
    private String titleError;

    public AddTimetableViewModel(@NonNull Application application) {
        super(application);
        this.updateDataInAllTimetablesFragmentLiveEvent = new SingleLiveEvent<>();
        this.detailEditing = false;
    }

    public SingleLiveEvent<Void> getUpdateDataInAllTimetablesFragmentLiveEvent() {
        return updateDataInAllTimetablesFragmentLiveEvent;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getTitleError() {
        return titleError;
    }

    private void setTitleError(String titleError) {
        this.titleError = titleError;
        notifyPropertyChanged(BR.titleError);
    }

    public void initEditTimetableRecyclerViewAdapter() {
        setAdapter(new EditTimetableRecyclerViewAdapter(new ArrayList<>()));
    }

    @Override
    public boolean onOptionsItemSelected(int itemId) {
        switch (itemId) {
            case R.id.toolbar_add:
                onAddTimetableClick();
                return true;
            case R.id.toolbar_cancel:
                onCancelClick();
                return true;
        }
        return false;
    }

    public void onAddTimetableClick() {
        if (title == null || title.isEmpty()) {
            showIncorrectTitleMessage();
            return;
        }
        try {
            List<Lesson> lessons = getLessonsFromRecyclerViewAdapter();
            if (lessons != null && !lessons.isEmpty()) {
                Timetable timetable = new Timetable(title, lessons);
                FakeTimetableRepository.getInstance().add(timetable);
                getUpdateDataInAllTimetablesFragmentLiveEvent().call();
                getPressBackEvent().call();
            } else {
                //todo
            }
        } catch (IncorrectInputException ignored) {
        }
    }

    private void showIncorrectTitleMessage() {
        setTitleError(
                getApplication().getString(R.string.incorrect_timetable_title)
        );
    }

    public void onCancelClick() {
        getPressBackEvent().call();
    }
}
