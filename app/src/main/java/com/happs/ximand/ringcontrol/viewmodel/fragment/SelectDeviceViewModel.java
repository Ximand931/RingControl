package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.BluetoothDao;
import com.happs.ximand.ringcontrol.model.dao.BluetoothEventListener;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;
import com.happs.ximand.ringcontrol.viewmodel.SnackbarDto;

import java.util.ArrayList;
import java.util.List;

public class SelectDeviceViewModel extends BaseViewModel {

    private final MutableLiveData<List<BluetoothDevice>> devicesLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Void> startSettingsActivityLiveEvent = new SingleLiveEvent<>();

    private final ObservableBoolean connecting = new ObservableBoolean();

    private final BluetoothEventListener<BluetoothEvent> infoEventListener;

    public SelectDeviceViewModel() {
        devicesLiveData.setValue(getBondedDevicesList());
        this.infoEventListener = new BluetoothEventListener<>(this::onInfoEvent);
        BluetoothDao.getInstance().subscribeToInfoEvents(infoEventListener);
    }

    public LiveData<List<BluetoothDevice>> getDevicesLiveData() {
        return devicesLiveData;
    }

    public SingleLiveEvent<Void> getStartSettingsActivityLiveEvent() {
        return startSettingsActivityLiveEvent;
    }

    public ObservableBoolean getConnectingAddress() {
        return connecting;
    }

    private void onInfoEvent(BluetoothEvent infoEvent) {
        if (infoEvent != BluetoothEvent.CONNECTING && connecting.get()) {
            connecting.set(false);
        }
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
        BluetoothDao.getInstance().startConnectToDeviceTask(device);
        connecting.set(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        BluetoothDao.getInstance().unsubscribeFromInfoEvents(infoEventListener);
    }
}
