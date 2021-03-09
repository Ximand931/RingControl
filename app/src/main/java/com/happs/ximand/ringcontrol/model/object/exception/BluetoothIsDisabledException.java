package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

@Deprecated
public class BluetoothIsDisabledException extends BluetoothExceptionDep {

    @Override
    public int getMessageResId() {
        return R.string.bluetooth_is_disabled;
    }
}
