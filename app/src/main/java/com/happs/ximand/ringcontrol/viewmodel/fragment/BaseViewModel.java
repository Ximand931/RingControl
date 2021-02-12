package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModel;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;

public abstract class BaseViewModel extends ViewModel implements LifecycleObserver {

    private final SingleLiveEvent<SnackbarDto> makeSnackbarEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<SnackbarDto> getMakeSnackbarEvent() {
        return makeSnackbarEvent;
    }

    public boolean notifyOptionsMenuItemClicked(int itemId) {
        return false;
    }
}
