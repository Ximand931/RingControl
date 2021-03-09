package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

@Deprecated
public class FailedToConnectException extends BluetoothExceptionDep {

    @Override
    public int getMessageResId() {
        return R.string.failed_to_connect;
    }

}
