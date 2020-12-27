package com.happs.ximand.ringcontrol.viewmodel.dto;

import com.happs.ximand.ringcontrol.OnEventListener;

public class InputAlertDialogDto {

    private int titleResId;
    private OnEventListener<String> inputCompleteListener;
    private OnEventListener<Void> cancelListener;

    public int getTitleResId() {
        return titleResId;
    }

    public InputAlertDialogDto setTitleResId(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public OnEventListener<String> getInputCompleteListener() {
        return inputCompleteListener;
    }

    public InputAlertDialogDto setInputCompleteListener(OnEventListener<String> inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
        return this;
    }

    public OnEventListener<Void> getCancelListener() {
        return cancelListener;
    }

    public InputAlertDialogDto setCancelListener(OnEventListener<Void> cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }
}
