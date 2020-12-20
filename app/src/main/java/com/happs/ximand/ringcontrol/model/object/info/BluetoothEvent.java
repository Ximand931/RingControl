package com.happs.ximand.ringcontrol.model.object.info;

import com.happs.ximand.ringcontrol.R;

public enum BluetoothEvent {
    CONNECTING(R.string.connecting_to_device),
    CONNECTED(R.string.connected),
    ERROR_WHILE_CONNECTING(R.string.error_while_connecting),
    FAILED_TO_CONNECT(R.string.failed_to_connect);

    private int messageResId;

    BluetoothEvent(int messageResId) {
        this.messageResId = messageResId;
    }

    public int getMessageResId() {
        return messageResId;
    }
}
