package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.bl.BluetoothDao;

import java.util.ArrayList;
import java.util.List;

public class SelectDeviceViewModel extends BaseViewModel {

    private MutableLiveData<List<BluetoothDevice>> devicesLiveData = new MutableLiveData<>();
    private SingleLiveEvent<Void> startSettingsActivityLiveEvent = new SingleLiveEvent<>();

    public SelectDeviceViewModel() {
        devicesLiveData.setValue(getBondedDevicesList());
    }

    public LiveData<List<BluetoothDevice>> getDevicesLiveData() {
        return devicesLiveData;
    }

    public SingleLiveEvent<Void> getStartSettingsActivityLiveEvent() {
        return startSettingsActivityLiveEvent;
    }

    public void updateData() {
        this.devicesLiveData.setValue(getBondedDevicesList());
    }

    private List<BluetoothDevice> getBondedDevicesList() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return new ArrayList<>(adapter.getBondedDevices());
    }

    public void startSettingsActivity() {
        startSettingsActivityLiveEvent.call();
    }

    public void notifyDeviceSelected(BluetoothDevice device) {
        BluetoothDao.getInstance().startConnectToDeviceTask(device);
    }

}
