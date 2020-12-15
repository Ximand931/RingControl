package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.ViewModel;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.viewmodel.SnackbarDto;

public abstract class BaseViewModel extends ViewModel {

    private final SingleLiveEvent<SnackbarDto> makeSnackbarEvent = new SingleLiveEvent<>();

    public boolean notifyOptionsMenuItemClicked(int itemId) {
        return false;
    }

    public SingleLiveEvent<SnackbarDto> getMakeSnackbarEvent() {
        return makeSnackbarEvent;
    }
}
