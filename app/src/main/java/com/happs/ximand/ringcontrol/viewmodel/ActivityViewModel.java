package com.happs.ximand.ringcontrol.viewmodel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothIsDisabledException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothNotSupportedException;
import com.happs.ximand.ringcontrol.model.object.exception.DeviceNotBoundedException;
import com.happs.ximand.ringcontrol.model.object.exception.DeviceNotFoundException;
import com.happs.ximand.ringcontrol.model.object.exception.DiscoveryModeIsNotStartedException;
import com.happs.ximand.ringcontrol.model.object.exception.FailedToConnectException;
import com.happs.ximand.ringcontrol.model.object.exception.LocationPermissionDeniedException;
import com.happs.ximand.ringcontrol.model.writer.QueueWriter;
import com.happs.ximand.ringcontrol.view.fragment.ExceptionFragment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

public class ActivityViewModel extends ViewModel {

    private static final int TIME_TO_SEARCH = 20000;
    private static final String DEFAULT_DEVICE_NAME = "STUB";

    private final MutableLiveData<ConnectStatus> connectStatusLiveData;
    private final SingleLiveEvent<Void> enableBluetoothRequestEvent;
    private final SingleLiveEvent<BroadcastReceiver> registerReceiverEvent;
    private final BluetoothAdapter bluetoothAdapter;

    private BluetoothDevice bluetoothDevice;

    public ActivityViewModel() {
        this.connectStatusLiveData = new MutableLiveData<>();
        this.enableBluetoothRequestEvent = new SingleLiveEvent<>();
        this.registerReceiverEvent = new SingleLiveEvent<>();
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startConnecting();
    }

    public MutableLiveData<ConnectStatus> getConnectStatusLiveData() {
        return connectStatusLiveData;
    }

    public SingleLiveEvent<Void> getEnableBluetoothRequestEvent() {
        return enableBluetoothRequestEvent;
    }

    public SingleLiveEvent<BroadcastReceiver> getRegisterReceiverEvent() {
        return registerReceiverEvent;
    }

    public boolean isTimetableApplyingPossible() {
        return connectStatusLiveData.getValue() == ConnectStatus.CONNECTED;
    }

    private void startConnecting() {
        try {
            startConnectingBluetoothDevice();
        } catch (BluetoothException e) {
            FragmentNavigation.getInstance()
                    .navigateToFragment(ExceptionFragment.newInstance(e));
        }
    }

    private void startConnectingBluetoothDevice() throws BluetoothException {
        connectStatusLiveData.setValue(ConnectStatus.WAITING_FOR_BLUETOOTH);

        if (bluetoothAdapter == null) {
            throw new BluetoothNotSupportedException();
        }
        if (!bluetoothAdapter.isEnabled()) {
            enableBluetoothRequestEvent.call();
        }
        try {
            searchDeviceInBoundenDevices();
            connectToDevice();
        } catch (DeviceNotBoundedException e) {
            startConnectingUnboundedDevice();
        }
        //setTimeLimit(); TODO
        new Handler().postDelayed(() -> {
            connectStatusLiveData.setValue(ConnectStatus.CONNECTED); //TODO: TEST
        }, 6000);
    }

    public void notifyUserEnabledBluetooth() {
        startConnecting();
    }

    public void notifyUserRefusedToEnableBluetooth() {
        onException(new BluetoothIsDisabledException());
    }

    private void searchDeviceInBoundenDevices() throws DeviceNotBoundedException {
        Set<BluetoothDevice> boundedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : boundedDevices) {
            if (device != null && device.getName() != null
                    && device.getName().equals(DEFAULT_DEVICE_NAME)) {
                this.bluetoothDevice = device;
            }
        }
        throw new DeviceNotBoundedException();
    }

    private void startConnectingUnboundedDevice() throws DiscoveryModeIsNotStartedException {
        registerReceiverEvent.setValue(getBluetoothBroadcastReceiver());
        boolean isStartedSuccessful = bluetoothAdapter.startDiscovery();
        if (isStartedSuccessful) {
            connectStatusLiveData.setValue(ConnectStatus.SEARCHING);
        } else {
            Log.e(getClass().getSimpleName(), "Bluetooth adapter: can not start discovery");
            throw new DiscoveryModeIsNotStartedException();
        }
    }

    public void notifyUserRefuseToProvideLocation() {
        onException(new LocationPermissionDeniedException());
    }

    private BroadcastReceiver getBluetoothBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (isSearchedDevice(device)) {
                        onDeviceFound(device);
                    }
                }
            }
        };
    }

    private boolean isSearchedDevice(BluetoothDevice device) {
        return device != null && device.getName() != null
                && device.getName().equals(DEFAULT_DEVICE_NAME);
    }

    private void onDeviceFound(BluetoothDevice device) {
        this.bluetoothDevice = device;
        bluetoothAdapter.cancelDiscovery();
        connectToDevice();
    }

    private void connectToDevice() {
        ConnectingToDeviceAsyncTask connectingTask =
                new ConnectingToDeviceAsyncTask(this::onDeviceConnected);
        connectingTask.execute(bluetoothDevice);
        connectStatusLiveData.setValue(ConnectStatus.CONNECTING);
    }

    private void onDeviceConnected(BluetoothSocket socket) {
        if (socket == null) {
            onException(new FailedToConnectException());
            return;
        }
        try {
            OutputStream deviceOutputStream = socket.getOutputStream();
            QueueWriter.initialized(deviceOutputStream);
            connectStatusLiveData.setValue(ConnectStatus.CONNECTED);
            Log.i(getClass().getSimpleName(), "Queue writer initialized successful");
        } catch (IOException e) {
            onException(new FailedToConnectException());
        }
    }

    private void setTimeLimit() {
        new Handler().postDelayed(() -> {
            if (bluetoothDevice == null) {
                bluetoothAdapter.cancelDiscovery();
                onException(new DeviceNotFoundException());
            }
        }, TIME_TO_SEARCH);
    }

    private void onException(BluetoothException e) {
        connectStatusLiveData.setValue(ConnectStatus.ERROR);
        ExceptionFragment fragment = ExceptionFragment.newInstance(e);
        FragmentNavigation.getInstance().navigateToFragment(fragment);
    }

    @Override
    protected void onCleared() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    private static class ConnectingToDeviceAsyncTask
            extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {

        private static final java.util.UUID UUID = java.util.UUID
                .fromString("00000000-0000-1000-8000-00805F9B34FB");

        private OnEventListener<BluetoothSocket> onConnected;

        public ConnectingToDeviceAsyncTask(OnEventListener<BluetoothSocket> onConnected) {
            this.onConnected = onConnected;
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            BluetoothDevice device = bluetoothDevices[0];
            try {
                BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(UUID);
                socket.connect();
                return socket;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(BluetoothSocket bluetoothSocket) {
            super.onPostExecute(bluetoothSocket);
            onConnected.onEvent(bluetoothSocket);
        }
    }
}
