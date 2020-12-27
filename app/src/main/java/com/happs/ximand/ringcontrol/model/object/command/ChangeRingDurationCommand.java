package com.happs.ximand.ringcontrol.model.object.command;

public class ChangeRingDurationCommand extends BluetoothCommand {

    private static final byte COMMAND_CODE = 20;

    private final int duration;

    public ChangeRingDurationCommand(int duration) {
        super(COMMAND_CODE);
        this.duration = duration;
    }

    @Override
    public byte[] getCommand() {
        byte low = (byte) (duration & 0xff);
        byte high = (byte) ((duration >> 8) & 0xff);
        return new byte[]{getCode(), low, high};
    }
}
