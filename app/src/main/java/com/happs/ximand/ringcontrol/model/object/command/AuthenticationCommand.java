package com.happs.ximand.ringcontrol.model.object.command;

public class AuthenticationCommand extends BluetoothCommand {

    private static final String AUTHENTICATE_COMMAND_CODE = "00";

    protected AuthenticationCommand() {
        super(AUTHENTICATE_COMMAND_CODE);
    }

    @Override
    public String getCommand() {
        //return getCode() + ;
        return null;
    }
}
