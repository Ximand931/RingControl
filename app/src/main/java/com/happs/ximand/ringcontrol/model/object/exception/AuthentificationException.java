package com.happs.ximand.ringcontrol.model.object.exception;

import com.happs.ximand.ringcontrol.R;

public class AuthentificationException extends BluetoothException {

    @Override
    public int getMessageResId() {
        return R.string.auth_error;
    }
}
