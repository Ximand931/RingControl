package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;

public class AddTimetableViewModel extends BaseEditTimetableViewModel {

    private final SingleLiveEvent<Void> updateDataInAllTimetablesFragmentLiveEvent;

    private final MutableLiveData<String> titleLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> titleErrorLiveData = new MutableLiveData<>();

    @Deprecated
    private String title;
    @Deprecated
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
        //TODO
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
        }
        //TODO;
    }

    private void showIncorrectTitleMessage() {
        setTitleError(
                getApplication().getString(R.string.incorrect_timetable_title)
        );
    }

    public void onCancelClick() {
        FragmentNavigation.getInstance().navigateToPreviousFragment();
    }
}
