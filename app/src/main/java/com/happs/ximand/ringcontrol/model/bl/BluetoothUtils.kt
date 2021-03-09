package com.happs.ximand.ringcontrol.model.bl

import android.bluetooth.BluetoothAdapter

object BluetoothUtils {

    fun getBluetoothAdapter(): BluetoothAdapter {
        return checkNotNull(BluetoothAdapter.getDefaultAdapter()) {
            "Device not support bluetooth"
        }
    }

}