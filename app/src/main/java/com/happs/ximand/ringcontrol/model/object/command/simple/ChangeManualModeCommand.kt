package com.happs.ximand.ringcontrol.model.`object`.command.simple

class ChangeManualModeCommand(value: Boolean) :
        SimpleBluetoothCommand(CODE, (if (value) 0xff else 0).toByte()) {

    companion object {
        const val CODE: Byte = 22
    }
}