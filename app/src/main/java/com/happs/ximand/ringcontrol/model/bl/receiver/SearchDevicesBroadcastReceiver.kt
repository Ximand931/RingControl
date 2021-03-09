package com.happs.ximand.ringcontrol.model.bl.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.happs.ximand.ringcontrol.model.bl.callback.SearchCallback
import com.happs.ximand.ringcontrol.model.bl.exception.BluetoothIsDisabledException

class SearchDevicesBroadcastReceiver : BroadcastReceiver() {

    var searchCallback: SearchCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            if (state == BluetoothAdapter.STATE_OFF) {
                searchCallback?.onException(BluetoothIsDisabledException())
            }
        } else if (intent.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
            context?.unregisterReceiver(this)
            searchCallback?.onFinish()
        } else if (intent.action == BluetoothDevice.ACTION_FOUND) {
            val device = intent
                    .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device != null) {
                searchCallback?.onFound(device)
            }
        }
    }

}