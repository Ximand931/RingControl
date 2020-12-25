package com.happs.ximand.ringcontrol.model.object.command;

public class AddTrustedDeviceCommand extends BluetoothCommand {

    private static final byte ADD_TRUSTED_DEVICE_COMMAND_CODE = 1;

    protected AddTrustedDeviceCommand() {
        super(ADD_TRUSTED_DEVICE_COMMAND_CODE);
    }

    @Override
    public byte[] getCommand() {
        return null;
    }
}
