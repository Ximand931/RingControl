package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

@Deprecated
public class WhileSendingException extends BluetoothExceptionDep {

    @Override
    public int getMessageResId() {
        return R.string.error_while_sending_data;
    }
}
