package com.happs.ximand.ringcontrol.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.dao.BluetoothDao;
import com.happs.ximand.ringcontrol.model.dao.BluetoothEventListener;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothIsDisabledException;
import com.happs.ximand.ringcontrol.model.object.info.BluetoothEvent;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.view.fragment.ExceptionFragment;
import com.happs.ximand.ringcontrol.view.fragment.SelectDeviceFragment;

public class ActivityViewModel extends ViewModel {

    private final BluetoothDao bluetoothDao = BluetoothDao.getInstance();

    private final MutableLiveData<Integer> infoLiveData = new MutableLiveData<>();

    private final BluetoothEventListener<BluetoothEvent> infoEventListener;
    private final BluetoothEventListener<BluetoothException> exceptionEventListener;

    private final SingleLiveEvent<Void> enableBluetoothLiveEvent = new SingleLiveEvent<>();

    private int exceptionCounter = 0;

    public ActivityViewModel() {
        this.infoEventListener = new BluetoothEventListener<>(this::onInfoEvent);
        this.exceptionEventListener = new BluetoothEventListener<>(this::onExceptionEvent);
    }

    public LiveData<Integer> getInfoLiveData() {
        return infoLiveData;
    }

    public SingleLiveEvent<Void> getEnableBluetoothLiveEvent() {
        return enableBluetoothLiveEvent;
    }

    private void onInfoEvent(BluetoothEvent infoEvent) {
        infoLiveData.postValue(infoEvent.getMessageResId());
    }

    private void onExceptionEvent(BluetoothException e) {
        exceptionCounter++;
        showExceptionFragmentIfManyErrors(e);
    }

    private void showExceptionFragmentIfManyErrors(BluetoothException e) {
        if (exceptionCounter >= 3) {
            FragmentNavigation.getInstance().navigateToFragment(ExceptionFragment.newInstance(e));
        }
    }

    public void afterOnCreate() {
        if (bluetoothDao.isBluetoothEnable()) {
            onBluetoothEnabled();
        } else {
            enableBluetoothLiveEvent.call();
        }
    }

    public void onBluetoothEnabled() {
        subscribeToBluetoothEvents();
        navigateToFirstFragment();
    }

    private void subscribeToBluetoothEvents() {
        bluetoothDao.subscribeToInfoEvents(infoEventListener);
        bluetoothDao.subscribeToExceptionEvents(exceptionEventListener);
    }

    public void onRefuseEnableBluetooth() {
        FragmentNavigation.getInstance().navigateToFragment(
                ExceptionFragment.newInstance(new BluetoothIsDisabledException())
        );
    }

    private void navigateToFirstFragment() {
        FragmentNavigation.getInstance().navigateToFragment(getFirstFragment());
    }

    @SuppressWarnings("rawtypes")
    private BaseFragment getFirstFragment() {
        if (SharedPreferencesDao.getInstance().getTargetDeviceAddress() != null) {
            return AllTimetablesFragment.newInstance();
        } else {
            return SelectDeviceFragment.newInstance();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        bluetoothDao.unsubscribeFromInfoEvents(infoEventListener);
        bluetoothDao.unsubscribeFromExceptionEvents(exceptionEventListener);
    }
}
