package com.happs.ximand.ringcontrol.model.object.command;

public abstract class BluetoothCommand {

    private String code;

    protected BluetoothCommand(String code) {
        this.code = code;
    }

    public abstract String getCommand();

    protected String getCode() {
        return code;
    }
}
