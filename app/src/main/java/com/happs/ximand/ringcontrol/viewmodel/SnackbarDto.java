package com.happs.ximand.ringcontrol.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SnackbarDto {

    public static final int ACTION_NONE = 0;

    private int messageResId;
    private int duration;
    private int actionId = ACTION_NONE;
    @Nullable
    private View.OnClickListener actionClickListener;

    public SnackbarDto(int messageResId, int duration) {
        this.messageResId = messageResId;
        this.duration = duration;
    }

    public int getMessageResId() {
        return messageResId;
    }

    public int getDuration() {
        return duration;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @Nullable
    public View.OnClickListener getActionClickListener() {
        return actionClickListener;
    }

    public void setActionClickListener(@NonNull View.OnClickListener actionClickListener) {
        this.actionClickListener = actionClickListener;
    }
}
