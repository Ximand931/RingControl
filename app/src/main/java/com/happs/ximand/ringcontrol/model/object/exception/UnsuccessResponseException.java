package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

public class UnsuccessResponseException extends BluetoothException {

    private final int code;

    public UnsuccessResponseException(int code) {
        this.code = code;
    }

    @Override
    public int getMessageResId() {
        return R.string.unsuccess_response;
    }

    @Override
    public int getCode() {
        return code;
    }
}
