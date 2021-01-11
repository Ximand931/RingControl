package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

public class BluetoothIsDisabledException extends BluetoothException {

    @Override
    public int getMessageResId() {
        return R.string.bluetooth_is_disabled;
    }
}
