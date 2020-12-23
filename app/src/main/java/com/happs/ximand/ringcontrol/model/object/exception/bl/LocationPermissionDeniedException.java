package com.happs.ximand.ringcontrol.model.object.exception.bl;

import com.happs.ximand.ringcontrol.R;

public class LocationPermissionDeniedException extends BluetoothException {

    @Override
    public int getMessageResId() {
        return R.string.location_permission_denied;
    }

}
