package com.happs.ximand.ringcontrol.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.SingleLiveEvent;
import com.happs.ximand.ringcontrol.model.bl.BluetoothDao;
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.view.fragment.SelectDeviceFragment;

public class ActivityViewModel extends ViewModel {

    private final MutableLiveData<ConnectStatus> connectStatusLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Void> enableBluetoothLiveEvent = new SingleLiveEvent<>();

    public ActivityViewModel() {
        this.connectStatusLiveData.setValue(ConnectStatus.SEARCHING);
    }

    public MutableLiveData<ConnectStatus> getConnectStatusLiveData() {
        return connectStatusLiveData;
    }

    public SingleLiveEvent<Void> getEnableBluetoothLiveEvent() {
        return enableBluetoothLiveEvent;
    }

    public void afterOnCreate() {
        if (BluetoothDao.getInstance().isBluetoothEnable()) {
            navigateToFirstFragment();
        } else {
            enableBluetoothLiveEvent.call();
        }
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


}
