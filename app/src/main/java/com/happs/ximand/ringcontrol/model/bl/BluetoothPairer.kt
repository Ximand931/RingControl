package com.happs.ximand.ringcontrol.model.bl

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import com.happs.ximand.ringcontrol.model.bl.callback.PairCallback
import com.happs.ximand.ringcontrol.model.bl.exception.PairingDeviceException
import com.happs.ximand.ringcontrol.model.bl.receiver.PairBroadcastReceiver

class BluetoothPairer(private val activity: Activity) {

    private val pairBroadcastReceiver = PairBroadcastReceiver()

    var pairCallback: PairCallback? = null

    fun pairDevice(device: BluetoothDevice) {
        pairBroadcastReceiver.device = device
        activity.registerReceiver(pairBroadcastReceiver,
                IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
        try {
            val method = device.javaClass.getMethod("createBond", null)
            method.invoke(device, null)
        } catch (e: Exception) {
            pairCallback?.onException(PairingDeviceException(e))
        }
    }

    fun unpairDevice(device: BluetoothDevice) {
        pairBroadcastReceiver.device = device
        try {
            val method = device.javaClass.getMethod("removeBond", null)
            method.invoke(device, null)
        } catch (e: Exception) {
            pairCallback?.onException(PairingDeviceException(e))
        }
    }

}