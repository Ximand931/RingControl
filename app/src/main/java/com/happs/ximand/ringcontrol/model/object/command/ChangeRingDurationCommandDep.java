package com.happs.ximand.ringcontrol.model.object.command;

@Deprecated
public class ChangeRingDurationCommandDep extends BluetoothCommand<Integer> {

    public static final byte COMMAND_CODE = 20;

    private final int duration;

    public ChangeRingDurationCommandDep(int duration) {
        super(COMMAND_CODE);
        this.duration = duration;
    }

    @Override
    public byte[] toByteArray() {
        byte low = (byte) (duration & 0xff);
        byte high = (byte) ((duration >> 8) & 0xff);
        return new byte[]{getCode(), low, high};
    }

    @Override
    public Integer getMainContent() {
        return duration;
    }
}
