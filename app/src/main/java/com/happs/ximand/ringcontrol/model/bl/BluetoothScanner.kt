package com.happs.ximand.ringcontrol.model.bl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import com.happs.ximand.ringcontrol.model.bl.callback.SearchCallback
import com.happs.ximand.ringcontrol.model.bl.exception.DiscoveryModeNotStartedException
import com.happs.ximand.ringcontrol.model.bl.receiver.SearchDevicesBroadcastReceiver

class BluetoothScanner(private val activity: Activity) {

    private val bluetoothAdapter = BluetoothUtils.getBluetoothAdapter()
    private val searchBroadcastReceiver = SearchDevicesBroadcastReceiver()

    var searchCallback: SearchCallback? = null

    fun searchDevices() {
        val filter = makeIntentFilterForSearchBluetoothDevices()
        activity.registerReceiver(searchBroadcastReceiver, filter)
        val success = bluetoothAdapter.startDiscovery()
        if (!success) {
            searchCallback?.onException(DiscoveryModeNotStartedException())
        }
    }

    private fun makeIntentFilterForSearchBluetoothDevices(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        return filter
    }

}