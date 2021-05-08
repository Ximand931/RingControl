package com.happs.ximand.ringcontrol.model.bl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import android.os.Handler
import com.happs.ximand.ringcontrol.model.bl.callback.SearchCallback
import com.happs.ximand.ringcontrol.model.bl.exception.DiscoveryModeNotStartedException
import com.happs.ximand.ringcontrol.model.bl.receiver.SearchDevicesBroadcastReceiver
import java.lang.ref.WeakReference

class BluetoothScanner(applicationContext: Context) {

    private val contextRef = WeakReference<Context>(applicationContext)
    private val bluetoothAdapter = BluetoothUtils.getBluetoothAdapter()
    private val searchBroadcastReceiver = SearchDevicesBroadcastReceiver()

    var timeToSearch = 10000L
    var searchCallback: SearchCallback? = null

    companion object {
        lateinit var instance: BluetoothScanner

        fun initialize(applicationContext: Context) {
            instance = BluetoothScanner(applicationContext)
        }
    }

    fun isCurrentScanning(): Boolean {
        return bluetoothAdapter.isDiscovering
    }

    fun startDevicesSearching() {
        val filter = makeIntentFilterForSearchBluetoothDevices()
        val context = contextRef.get() ?: throw IllegalStateException()
        context.registerReceiver(searchBroadcastReceiver, filter)
        val success = bluetoothAdapter.startDiscovery()
        searchBroadcastReceiver.searchCallback = searchCallback
        if (!success) {
            searchCallback?.onException(DiscoveryModeNotStartedException())
        }
        Handler().postDelayed({
            bluetoothAdapter.cancelDiscovery()
            context.unregisterReceiver(searchBroadcastReceiver)
        }, timeToSearch)
    }

    private fun makeIntentFilterForSearchBluetoothDevices(): IntentFilter {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        return filter
    }

}