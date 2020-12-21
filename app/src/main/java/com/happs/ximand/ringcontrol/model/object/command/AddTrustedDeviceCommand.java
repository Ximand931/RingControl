package com.happs.ximand.ringcontrol.model.object.command;

public class AddTrustedDeviceCommand extends BluetoothCommand {

    private static final String ADD_TRUSTED_DEVICE_COMMAND_CODE = "01";

    protected AddTrustedDeviceCommand() {
        super(ADD_TRUSTED_DEVICE_COMMAND_CODE);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
