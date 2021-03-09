package com.happs.ximand.ringcontrol.model.object.info;

import com.happs.ximand.ringcontrol.R;

@Deprecated
public enum BluetoothEvent {
    CONNECTING(R.string.connecting_to_device),
    AUTHENTICATION(R.string.auth),
    @Deprecated CONNECTED(R.string.connected),
    READY(R.string.ready),
    SENDING_DATA(R.string.sending_data),
    ERROR_WHILE_CONNECTING(R.string.error_while_connecting),
    FAILED_TO_CONNECT(R.string.failed_to_connect),
    ERROR_WHILE_SENDING(R.string.error_while_sending_data);

    private final int messageResId;

    BluetoothEvent(int messageResId) {
        this.messageResId = messageResId;
    }

    public int getMessageResId() {
        return messageResId;
    }
}
