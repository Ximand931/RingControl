package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

public class SelectDeviceViewModel extends BaseViewModel implements Bluetooth.DiscoveryCallback, Bluetooth.CommunicationCallback {

    private final MutableLiveData<List<BluetoothDevice>> devicesLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Void> startSettingsActivityLiveEvent = new SingleLiveEvent<>();
    private final ObservableBoolean searching = new ObservableBoolean();
    private final ObservableBoolean connecting = new ObservableBoolean();
    @Nullable
    private BluetoothDevice connectingDevice;

    public SelectDeviceViewModel() {
        devicesLiveData.setValue(new ArrayList<>());
        BluetoothNDao.getInstance().startScanning(this);
    }

    public LiveData<List<BluetoothDevice>> getDevicesLiveData() {
        return devicesLiveData;
    }

    public SingleLiveEvent<Void> getStartSettingsActivityLiveEvent() {
        return startSettingsActivityLiveEvent;
    }

    public ObservableBoolean getSearching() {
        return searching;
    }

    public ObservableBoolean getConnecting() {
        return connecting;
    }

    public void updateData() {
        this.devicesLiveData.setValue(getBondedDevicesList());
        getMakeSnackbarEvent().setValue(
                new SnackbarDto(R.string.data_updated, Snackbar.LENGTH_SHORT)
        );
    }

    private List<BluetoothDevice> getBondedDevicesList() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return new ArrayList<>(adapter.getBondedDevices());
    }

    public void startSettingsActivity() {
        startSettingsActivityLiveEvent.call();
    }

    public void notifyDeviceSelected(BluetoothDevice device) {
        connectingDevice = device;
        BluetoothNDao.getInstance().startConnecting(device, this);
        connectingDevice = device;
        connecting.set(true);
    }

    @Override
    public void onFinish() {
        searching.set(false);
    }

    @Override
    public void onDevice(BluetoothDevice device) {
        List<BluetoothDevice> devices = devicesLiveData.getValue();
        if (devices != null) {
            devices.add(device);
            devicesLiveData.postValue(devices);
        }
    }

    @Override
    public void onPair(BluetoothDevice device) {
        Log.d("...", "paired");
    }

    private void saveConnectedDeviceAddress() {
        if (connectingDevice != null) {
            SharedPreferencesDao.getInstance().updateTargetDeviceAddress(
                    connectingDevice.getAddress()
            );
        }
    }

    @Override
    public void onUnpair(BluetoothDevice device) {
        BluetoothNDao.getInstance().unpairDevice(device);
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        connecting.set(false);
        if (device != null) {
            BluetoothNDao.getInstance().setPairedDevice(device);
            FragmentNavigation.getInstance()
                    .navigateTo(AllTimetablesFragment.newInstance());
            saveConnectedDeviceAddress();
        }
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        BluetoothNDao.getInstance().startConnecting(device, null);
        Log.d("...", "retrying");
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onError(String message) {
        getMakeSnackbarEvent().setValue(
                new SnackbarDto(R.string.error_while_connecting, Snackbar.LENGTH_LONG)
        );
        searching.set(false);
    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

    }
}
