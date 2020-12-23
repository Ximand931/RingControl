package com.happs.ximand.ringcontrol.model.object.exception.bl;

import com.happs.ximand.ringcontrol.R;

public class FailedToConnectException extends BluetoothException {

    @Override
    public int getMessageResId() {
        return R.string.failed_to_connect;
    }

}
