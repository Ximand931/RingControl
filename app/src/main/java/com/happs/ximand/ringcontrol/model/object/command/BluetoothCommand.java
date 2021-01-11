package com.happs.ximand.ringcontrol.model.object.command;

public abstract class BluetoothCommand<T> {

    private final byte code;

    protected BluetoothCommand(byte code) {
        this.code = code;
    }

    public abstract byte[] getCommand();

    public abstract T getMainContent();

    protected byte getCode() {
        return code;
    }
}
