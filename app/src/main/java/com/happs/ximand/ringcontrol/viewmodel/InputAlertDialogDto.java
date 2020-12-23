package com.happs.ximand.ringcontrol.viewmodel;

import com.happs.ximand.ringcontrol.OnEventListener;

public class InputAlertDialogDto {

    private int titleResId;
    private OnEventListener<String> onCompleteInput;
    private OnEventListener<Void> onCancel;

    public int getTitleResId() {
        return titleResId;
    }

    public InputAlertDialogDto setTitleResId(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public OnEventListener<String> getOnCompleteInput() {
        return onCompleteInput;
    }

    public InputAlertDialogDto setOnCompleteInput(OnEventListener<String> onCompleteInput) {
        this.onCompleteInput = onCompleteInput;
        return this;
    }

    public OnEventListener<Void> getOnCancel() {
        return onCancel;
    }

    public InputAlertDialogDto setOnCancel(OnEventListener<Void> onCancel) {
        this.onCancel = onCancel;
        return this;
    }
}
