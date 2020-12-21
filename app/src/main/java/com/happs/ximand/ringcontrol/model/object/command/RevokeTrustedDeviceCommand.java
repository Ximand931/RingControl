package com.happs.ximand.ringcontrol.model.object.command;

public class RevokeTrustedDeviceCommand extends BluetoothCommand {

    private static final String REVOKE_TRUSTED_DEVICE_COMMAND_CODE = "02";

    protected RevokeTrustedDeviceCommand() {
        super(REVOKE_TRUSTED_DEVICE_COMMAND_CODE);
    }

    @Override
    public String getCommand() {
        return null;
    }
}
