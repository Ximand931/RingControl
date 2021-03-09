package com.happs.ximand.ringcontrol.model.`object`.command.simple

import com.happs.ximand.ringcontrol.model.`object`.command.BluetoothCommand

abstract class SimpleBluetoothCommand(code: Byte, val mainContent: Byte) : BluetoothCommand<Byte>(code) {

    override fun toByteArray(): ByteArray {
        return byteArrayOf(code, mainContent)
    }

    override fun getMainContent(): Byte {
        return mainContent
    }
}