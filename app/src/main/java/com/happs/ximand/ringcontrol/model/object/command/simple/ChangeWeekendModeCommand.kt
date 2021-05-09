package com.happs.ximand.ringcontrol.model.`object`.command.simple

class ChangeWeekendModeCommand(value: Byte) : SimpleBluetoothCommand(CODE, value) {

    companion object {
        const val CODE: Byte = 21
    }

    init {
        require(value <= 2)
    }
}