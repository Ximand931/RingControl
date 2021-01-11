package com.happs.ximand.ringcontrol.viewmodel.dto;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SnackbarDto {

    public static final int ACTION_NONE = 0;
    public static final int ICON_NONE = 0;

    private int messageResId;
    private int iconResId;
    private int duration;

    private int actionResId = ACTION_NONE;
    @Nullable
    private View.OnClickListener actionClickListener;

    private boolean format = false;
    private Object[] args;

    public SnackbarDto(int messageResId, int duration) {
        this.messageResId = messageResId;
        this.duration = duration;
    }

    public int getMessageResId() {
        return messageResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public SnackbarDto setIconResId(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public int getActionResId() {
        return actionResId;
    }

    public SnackbarDto setActionResId(int actionResId) {
        this.actionResId = actionResId;
        return this;
    }

    @Nullable
    public View.OnClickListener getActionClickListener() {
        return actionClickListener;
    }

    public SnackbarDto setActionClickListener(@NonNull View.OnClickListener actionClickListener) {
        this.actionClickListener = actionClickListener;
        return this;
    }

    public boolean isFormat() {
        return format;
    }

    public SnackbarDto setFormat(boolean format) {
        this.format = format;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public SnackbarDto setArgs(Object[] args) {
        this.args = args;
        return this;
    }
}
