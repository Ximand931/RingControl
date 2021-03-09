package com.happs.ximand.ringcontrol.model.bl.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.happs.ximand.ringcontrol.model.bl.callback.PairCallback

class PairBroadcastReceiver : BroadcastReceiver() {

    var pairCallback: PairCallback? = null
    var device: BluetoothDevice? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || device == null) {
            return
        }
        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == intent.action) {
            val state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)
            val prevState = intent.getIntExtra(
                    BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR
            )
            if (state == BluetoothDevice.BOND_BONDED &&
                    prevState == BluetoothDevice.BOND_BONDING) {
                context?.unregisterReceiver(this)
                pairCallback?.onPaired(device!!)
            } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                context?.unregisterReceiver(this)
                pairCallback?.onUnpaired(device!!)
            }
        }
    }

}