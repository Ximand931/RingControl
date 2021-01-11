package com.happs.ximand.ringcontrol.model.object.exception;

public abstract class BluetoothException extends Exception {

    public int getMessageResId() {
        return 0;
    }

    public int getCode() {
        return -1;
    }

}
