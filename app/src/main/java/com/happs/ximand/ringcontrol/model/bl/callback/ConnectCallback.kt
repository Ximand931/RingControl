package com.happs.ximand.ringcontrol.model.bl.callback

import android.bluetooth.BluetoothDevice

interface ConnectCallback : Callback {
    fun onConnected(device: BluetoothDevice)
}