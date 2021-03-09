package com.happs.ximand.ringcontrol.model.`object`.command.simple

class ChangeWeekendModeCommand(value: Byte) : SimpleBluetoothCommand(COMMAND_CODE, value) {

    companion object {
        const val COMMAND_CODE: Byte = 21
    }

    init {
        require(value <= 2)
    }
}