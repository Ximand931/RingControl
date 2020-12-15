package com.happs.ximand.ringcontrol.viewmodel;

public class SnackbarDto {

    private int messageResId;
    private int duration;

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

}
