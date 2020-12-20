package com.happs.ximand.ringcontrol.viewmodel;

import android.content.DialogInterface;

public class AlertDialogDto {

    private int titleResId;
    private int messageResId;
    private int positiveButtonResId;
    private int negativeButtonResId;
    private DialogInterface.OnClickListener onPositiveClick;

    public AlertDialogDto() {
    }

    public int getTitleResId() {
        return titleResId;
    }

    public AlertDialogDto setTitleResId(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public int getMessageResId() {
        return messageResId;
    }

    public AlertDialogDto setMessageResId(int messageResId) {
        this.messageResId = messageResId;
        return this;
    }

    public int getPositiveButtonResId() {
        return positiveButtonResId;
    }

    public AlertDialogDto setPositiveButtonResId(int positiveButtonResId) {
        this.positiveButtonResId = positiveButtonResId;
        return this;
    }

    public int getNegativeButtonResId() {
        return negativeButtonResId;
    }

    public AlertDialogDto setNegativeButtonResId(int negativeButtonResId) {
        this.negativeButtonResId = negativeButtonResId;
        return this;
    }

    public DialogInterface.OnClickListener getOnPositiveClick() {
        return onPositiveClick;
    }

    public AlertDialogDto setOnPositiveClick(DialogInterface.OnClickListener onPositiveClick) {
        this.onPositiveClick = onPositiveClick;
        return this;
    }
}
