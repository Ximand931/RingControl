package com.happs.ximand.ringcontrol.model.object.command.simple;

public class MakeRingCommand extends SimpleBluetoothCommand {

    private static final byte MAKE_RING_COMMAND = 31;

    public MakeRingCommand(byte seconds) {
        super(MAKE_RING_COMMAND, seconds);
    }

}
