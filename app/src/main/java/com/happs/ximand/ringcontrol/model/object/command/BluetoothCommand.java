package com.happs.ximand.ringcontrol.model.object.command;

public abstract class BluetoothCommand {

    private final byte code;

    protected BluetoothCommand(byte code) {
        this.code = code;
    }

    public abstract byte[] getCommand();

    protected byte getCode() {
        return code;
    }
}
