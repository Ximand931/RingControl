package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

public class WhileReadingException extends BluetoothException {

    @Override
    public int getMessageResId() {
        return R.string.error_while_reading;
    }
}
