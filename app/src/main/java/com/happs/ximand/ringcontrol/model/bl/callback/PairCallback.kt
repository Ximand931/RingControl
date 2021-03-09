package com.happs.ximand.ringcontrol.model.bl.callback

import android.bluetooth.BluetoothDevice

interface PairCallback : Callback {
    fun onPaired(device: BluetoothDevice)
    fun onUnpaired(device: BluetoothDevice)
}