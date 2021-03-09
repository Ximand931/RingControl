package com.happs.ximand.ringcontrol.model.bl.callback

import android.bluetooth.BluetoothDevice

interface SearchCallback : Callback {
    fun onFound(device: BluetoothDevice)
    fun onFinish()
}