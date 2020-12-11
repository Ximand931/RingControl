package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;

import java.util.ArrayList;

public class AddTimetableViewModel extends BaseEditTimetableViewModel {

    private final MutableLiveData<String> titleLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> titleErrorLiveData = new MutableLiveData<>();

    public AddTimetableViewModel() {
        this.detailEditing = false;
        setLessons(new ArrayList<>());
    }

    public MutableLiveData<String> getTitleLiveData() {
        return titleLiveData;
    }

    public MutableLiveData<Boolean> getTitleErrorLiveData() {
        return titleErrorLiveData;
    }

    @Override
    public boolean notifyOptionsMenuItemClicked(int itemId) {
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
        if (TextUtils.isEmpty(titleLiveData.getValue())) {
            titleErrorLiveData.setValue(true);
        }
    }

    public void onCancelClick() {
        FragmentNavigation.getInstance().navigateToPreviousFragment();
    }
}
