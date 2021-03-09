package com.happs.ximand.ringcontrol.model.`object`.command

class SendDateCommand(private val yearLastTwoDigits: Byte, private val month: Byte, private val dateOfMonth: Byte,
                      private val hours: Byte, private val minutes: Byte, private val seconds: Byte, private val dateOfWeek: Byte) : BluetoothCommand<String?>(SEND_TIME_COMMAND_CODE) {
    override fun toByteArray(): ByteArray {
        return byteArrayOf(
                code, yearLastTwoDigits, month, dateOfMonth, hours, minutes, seconds, dateOfWeek
        )
    }

    override fun getMainContent(): String {
        return "$hours:$minutes:$seconds"
    }

    companion object {
        private const val SEND_TIME_COMMAND_CODE: Byte = 30
    }

}