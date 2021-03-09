package com.happs.ximand.ringcontrol.model.object.exception;

@Deprecated
public abstract class BluetoothExceptionDep extends Exception {

    public int getMessageResId() {
        return 0;
    }

    public int getCode() {
        return -1;
    }

}
