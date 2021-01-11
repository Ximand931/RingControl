package com.happs.ximand.ringcontrol.model.object.command.simple;

public class ChangeWeekendModeCommand extends SimpleBluetoothCommand {

    public static final byte COMMAND_CODE = 21;

    public ChangeWeekendModeCommand(byte value) {
        super(COMMAND_CODE, value);
        if (value > 2) {
            throw new IllegalArgumentException();
        }
    }

}
