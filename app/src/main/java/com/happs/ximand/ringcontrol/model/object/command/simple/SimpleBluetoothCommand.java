package com.happs.ximand.ringcontrol.model.object.command.simple;

import com.happs.ximand.ringcontrol.model.object.command.BluetoothCommand;

public abstract class SimpleBluetoothCommand extends BluetoothCommand {

    private byte value;

    protected SimpleBluetoothCommand(byte code, byte value) {
        super(code);
    }

    @Override
    public byte[] getCommand() {
        return new byte[]{getCode(), value};
    }
}
