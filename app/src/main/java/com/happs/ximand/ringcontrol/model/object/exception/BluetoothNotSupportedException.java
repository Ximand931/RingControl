package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

public class BluetoothNotSupportedException extends BluetoothException {

    @Override
    public int getMessageResId() {
        return R.string.bluetooth_not_supported;
    }
}
