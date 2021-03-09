package com.happs.ximand.ringcontrol.model.bl

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.happs.ximand.ringcontrol.model.bl.callback.ConnectCallback
import com.happs.ximand.ringcontrol.model.bl.callback.ReceiveCallback
import com.happs.ximand.ringcontrol.model.bl.callback.SendCallback
import com.happs.ximand.ringcontrol.model.bl.thread.ConnectThread
import com.happs.ximand.ringcontrol.model.bl.thread.ReceiveThread
import java.io.IOException
import java.util.*

class BluetoothCommunicator {

    private val bluetoothAdapter = BluetoothUtils.getBluetoothAdapter()
    private var socket: BluetoothSocket? = null
    private var device: BluetoothDevice? = null

    var sendCallback: SendCallback? = null
    var receiveCallback: ReceiveCallback? = null
    var connectCallback: ConnectCallback? = null

    var connected: Boolean = false

    companion object {
        val UUID: UUID = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
        }
    }

    fun disableBluetooth() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }

    fun disconnect() {
        socket?.close()
    }

    fun connectToDevice(device: BluetoothDevice) {
        this.device = device
        ConnectThread(device, this::onConnected).start()
    }

    private fun onConnected(socket: BluetoothSocket) {
        this.connected = true
        this.socket = socket
        ReceiveThread(socket.inputStream).start()
    }

    fun writeBytes(byteArray: ByteArray) {
        if (socket != null && socket!!.outputStream != null) {
            try {
                socket!!.outputStream!!.write(byteArray)
                sendCallback?.onSent()
            } catch (e: IOException) {
                sendCallback?.onException(e)
            }
        } else {
            sendCallback?.onException(NullPointerException())
        }
    }
}