package com.happs.ximand.ringcontrol.model.`object`.command.simple

class MakeRingCommand(seconds: Byte) : SimpleBluetoothCommand(MAKE_RING_COMMAND, seconds) {
    companion object {
        private const val MAKE_RING_COMMAND: Byte = 31
    }

}