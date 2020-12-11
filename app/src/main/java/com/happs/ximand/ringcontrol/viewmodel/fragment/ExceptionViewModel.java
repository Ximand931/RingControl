package com.happs.ximand.ringcontrol.viewmodel.fragment;

import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothIsDisabledException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothNotSupportedException;
import com.happs.ximand.ringcontrol.model.object.exception.DeviceNotFoundException;
import com.happs.ximand.ringcontrol.model.object.exception.DiscoveryModeIsNotStartedException;
import com.happs.ximand.ringcontrol.model.object.exception.FailedToConnectException;
import com.happs.ximand.ringcontrol.model.object.exception.LocationPermissionDeniedException;

public class ExceptionViewModel extends BaseFragmentViewModel {

    private final SingleLiveEvent<Void> restartApplicationLiveEvent;

    private BluetoothException exception;

    public ExceptionViewModel() {
        this.restartApplicationLiveEvent = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<Void> getRestartApplicationLiveEvent() {
        return restartApplicationLiveEvent;
    }

    @Bindable
    public String getDescription() {
        if (exception instanceof BluetoothIsDisabledException) {
            return getApplication().getString(R.string.bluetooth_is_disabled);
        } else if (exception instanceof DeviceNotFoundException) {
            return getApplication().getString(R.string.device_not_found);
        } else if (exception instanceof LocationPermissionDeniedException) {
            return getApplication().getString(R.string.location_permission_denied);
        } else if (exception instanceof DiscoveryModeIsNotStartedException) {
            return getApplication().getString(R.string.bluetooth_was_not_initialized);
        } else if (exception instanceof BluetoothNotSupportedException) {
            return getApplication().getString(R.string.bluetooth_not_supported);
        } else if (exception instanceof FailedToConnectException) {
            return getApplication().getString(R.string.failed_to_connect);
        }
        return "Stub";
    }

    public void setException(BluetoothException exception) {
        this.exception = exception;
        notifyPropertyChanged(BR.description);
    }

    public void onRetryClick() {
        restartApplicationLiveEvent.call();
    }
}
