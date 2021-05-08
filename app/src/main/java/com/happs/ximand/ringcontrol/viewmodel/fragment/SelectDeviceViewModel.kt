package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException
import com.happs.ximand.ringcontrol.model.bl.BluetoothCommunicator
import com.happs.ximand.ringcontrol.model.bl.BluetoothScanner
import com.happs.ximand.ringcontrol.model.bl.callback.ConnectCallback
import com.happs.ximand.ringcontrol.model.bl.callback.SearchCallback
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment

class SelectDeviceViewModel : BaseViewModel(), SearchCallback, ConnectCallback {

    private val scanner = BluetoothScanner.instance
    private val communicator = BluetoothCommunicator.instance
    val devicesLiveData = MutableLiveData<MutableList<BluetoothDevice>>()

    var searchFinishedListener: (() -> Unit)? = null
    val connecting = ObservableBoolean()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        permissionRequest.value = ACCESS_FINE_LOCATION
    }

    fun updateData(applicationContext: Context, onFinish: () -> Unit) {
        if (scanner.isCurrentScanning()) {
            onFinish.invoke()
            return
        }
        preparePhoneForSearch()
        devicesLiveData.value = ArrayList()
        searchFinishedListener = onFinish
        scanner.searchCallback = this
        scanner.startDevicesSearching()
    }

    private fun preparePhoneForSearch() {
        if (!communicator.isBluetoothEnabled()) {
            communicator.enableBluetooth()
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        communicator.connectCallback = this
        communicator.connectToDevice(device)
    }

    override fun onFound(device: BluetoothDevice) {
        devicesLiveData.value?.add(device)
        devicesLiveData.value = devicesLiveData.value
    }

    override fun onFinish() {
        searchFinishedListener?.invoke()
    }

    override fun onConnected(device: BluetoothDevice) {
        connecting.set(true)
        FragmentNavigation.instance
                .navigateTo(AllTimetablesFragment.newInstance())
        SharedPreferencesDao.instance
                .updateTargetDeviceAddress(device.address)
    }

    override fun onException(e: BaseException) {
        makeExceptionSnackbarWithAction(R.string.unsuccessful_select, e)
        searchFinishedListener?.invoke()
    }

}