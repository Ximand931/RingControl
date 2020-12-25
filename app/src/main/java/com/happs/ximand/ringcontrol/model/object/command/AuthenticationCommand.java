package com.happs.ximand.ringcontrol.model.object.command;

public class AuthenticationCommand extends BluetoothCommand {

    private static final byte AUTHENTICATE_COMMAND_CODE = 0;

    protected AuthenticationCommand() {
        super(AUTHENTICATE_COMMAND_CODE);
    }

    @Override
    public byte[] getCommand() {
        //return getCode() + ;
        return null;
    }
}
