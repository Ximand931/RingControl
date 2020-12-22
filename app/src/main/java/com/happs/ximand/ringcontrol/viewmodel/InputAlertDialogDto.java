package com.happs.ximand.ringcontrol.viewmodel;

import com.happs.ximand.ringcontrol.OnEventListener;

public class InputAlertDialogDto {

    private int titleResId;
    private int positiveButtonResId;
    private int negativeButtonResId;
    private OnEventListener<String> onCompleteInput;

    public InputAlertDialogDto() {
    }

    public int getTitleResId() {
        return titleResId;
    }

    public InputAlertDialogDto setTitleResId(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public int getPositiveButtonResId() {
        return positiveButtonResId;
    }

    public InputAlertDialogDto setPositiveButtonResId(int positiveButtonResId) {
        this.positiveButtonResId = positiveButtonResId;
        return this;
    }

    public int getNegativeButtonResId() {
        return negativeButtonResId;
    }

    public InputAlertDialogDto setNegativeButtonResId(int negativeButtonResId) {
        this.negativeButtonResId = negativeButtonResId;
        return this;
    }

    public OnEventListener<String> getOnCompleteInput() {
        return onCompleteInput;
    }

    public InputAlertDialogDto setOnCompleteInput(OnEventListener<String> onCompleteInput) {
        this.onCompleteInput = onCompleteInput;
        return this;
    }
}
