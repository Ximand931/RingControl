package com.happs.ximand.ringcontrol.model.bl.thread

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.happs.ximand.ringcontrol.model.bl.BluetoothCommunicator
import com.happs.ximand.ringcontrol.model.bl.callback.ConnectCallback
import com.happs.ximand.ringcontrol.model.bl.exception.ConnectingException
import com.happs.ximand.ringcontrol.model.bl.exception.CreateSocketException
import java.io.IOException

class ConnectThread(private val device: BluetoothDevice,
                    private val onConnected: (socket: BluetoothSocket) -> Unit) : Thread() {

    var connectionCallback: ConnectCallback? = null

    private val bluetoothAdapter: BluetoothAdapter =
            checkNotNull(BluetoothAdapter.getDefaultAdapter()) {
                "Device not support bluetooth"
            }

    private var socket: BluetoothSocket? = null

    init {
        try {
            socket = device.createRfcommSocketToServiceRecord(BluetoothCommunicator.UUID)
        } catch (e: IOException) {
            connectionCallback?.onException(CreateSocketException(e))
        }
    }

    override fun run() {
        super.run()
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        if (socket != null) {
            try {
                socket!!.connect()
                connectionCallback?.onConnected(device)
                onConnected.invoke(socket!!)
            } catch (e: IOException) {
                connectionCallback?.onException(ConnectingException(e))
                socket!!.close()
            }
        }
    }
}