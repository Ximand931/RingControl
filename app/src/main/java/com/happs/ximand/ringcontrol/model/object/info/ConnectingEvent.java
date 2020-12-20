package com.happs.ximand.ringcontrol.model.object.info;

import com.happs.ximand.ringcontrol.R;

public class ConnectingEvent extends InfoEvent {
    @Override
    public int getMessageResId() {
        return R.string.connecting_to_device;
    }
}
