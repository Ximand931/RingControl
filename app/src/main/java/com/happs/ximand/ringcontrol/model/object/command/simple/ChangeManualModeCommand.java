package com.happs.ximand.ringcontrol.model.object.command.simple;

public class ChangeManualModeCommand extends SimpleBluetoothCommand {

    public static final byte COMMAND_CODE = 22;

    public ChangeManualModeCommand(boolean value) {
        super(COMMAND_CODE, ((byte) (value ? 0xff : 0)));
    }

}
