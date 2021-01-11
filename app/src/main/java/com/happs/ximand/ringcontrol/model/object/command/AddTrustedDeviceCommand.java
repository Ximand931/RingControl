package com.happs.ximand.ringcontrol.model.object.command;

public class AddTrustedDeviceCommand extends BluetoothCommand<String> {

    private static final byte COMMAND_CODE = 1;

    protected AddTrustedDeviceCommand() {
        super(COMMAND_CODE);
    }

    @Override
    public byte[] getCommand() {
        return null;
    }

    @Override
    public String getMainContent() {
        throw new UnsupportedOperationException();
    }
}
