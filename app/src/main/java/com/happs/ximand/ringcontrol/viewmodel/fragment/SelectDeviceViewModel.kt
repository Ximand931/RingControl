package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.exception.WhileConnectingException
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto
import me.aflak.bluetooth.Bluetooth.CommunicationCallback
import me.aflak.bluetooth.Bluetooth.DiscoveryCallback
import java.util.*

class SelectDeviceViewModel : BaseViewModel(), DiscoveryCallback, CommunicationCallback {
    val devicesLiveData = MutableLiveData<MutableList<BluetoothDevice>>()
    val startSettingsActivityLiveEvent = SingleLiveEvent<Void>()
    val searching = ObservableBoolean()
    val connecting = ObservableBoolean()
    private var connectingDevice: BluetoothDevice? = null
    fun getDevicesLiveData(): LiveData<MutableList<BluetoothDevice>> {
        return devicesLiveData
    }

    //TODO
    fun updateData() {
        devicesLiveData.value = bondedDevicesList
        makeSnackbarEvent.value = SnackbarDto(R.string.data_updated, Snackbar.LENGTH_SHORT)
    }

    private val bondedDevicesList: MutableList<BluetoothDevice>
        get() {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            return ArrayList(adapter.bondedDevices)
        }

    fun startSettingsActivity() {
        startSettingsActivityLiveEvent.call()
    }

    fun notifyDeviceSelected(device: BluetoothDevice?) {
        connectingDevice = device
        BluetoothNDao.getInstance().startConnecting(device, this)
        connectingDevice = device
        connecting.set(true)
    }

    override fun onFinish() {
        searching.set(false)
    }

    override fun onDevice(device: BluetoothDevice) {
        val devices = devicesLiveData.value
        if (devices != null) {
            devices.add(device)
            devicesLiveData.postValue(devices)
        }
    }

    override fun onPair(device: BluetoothDevice) {
        Log.d("...", "paired")
    }

    private fun saveConnectedDeviceAddress() {
        if (connectingDevice != null) {
            SharedPreferencesDao.getInstance().updateTargetDeviceAddress(
                    connectingDevice!!.address
            )
        }
    }

    override fun onUnpair(device: BluetoothDevice) {
        BluetoothNDao.getInstance().unpairDevice(device)
    }

    override fun onConnect(device: BluetoothDevice) {
        connecting.set(false)
        BluetoothNDao.getInstance().setPairedDevice(device)
        FragmentNavigation.getInstance()
                .navigateTo(AllTimetablesFragment.newInstance())
        saveConnectedDeviceAddress()
    }

    override fun onDisconnect(device: BluetoothDevice, message: String) {
        BluetoothNDao.getInstance().startConnecting(device, null)
        Log.d("...", "retrying")
    }

    override fun onMessage(message: String) {}

    override fun onError(message: String) {
        makeExceptionSnackbarWithAction(R.string.error_while_connecting,
                WhileConnectingException(IllegalStateException(message)))
        searching.set(false)
    }

    override fun onConnectError(device: BluetoothDevice, message: String) {}

    init {
        devicesLiveData.value = ArrayList()
        BluetoothNDao.getInstance().startScanning(this)
    }
}