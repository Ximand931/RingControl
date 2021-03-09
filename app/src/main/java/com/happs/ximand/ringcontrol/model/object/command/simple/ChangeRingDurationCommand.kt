package com.happs.ximand.ringcontrol.model.`object`.command.simple

class ChangeRingDurationCommand(value: Byte) : SimpleBluetoothCommand(CODE, value) {

    companion object {
        private const val CODE: Byte = 20
    }

}