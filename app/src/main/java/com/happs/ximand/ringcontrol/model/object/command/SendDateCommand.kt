package com.happs.ximand.ringcontrol.model.`object`.command

import java.util.Calendar.*

class SendDateCommand : BluetoothCommand<String?>(CODE) {

    private val yearLastTwoDigits: Byte
    private val month: Byte
    private val dateOfMonth: Byte
    private val hours: Byte
    private val minutes: Byte
    private val seconds: Byte
    private val dateOfWeek: Byte

    companion object {
        private const val CODE: Byte = 30
    }

    init {
        val calendar = getInstance()
        yearLastTwoDigits = (calendar[YEAR] % 100).toByte()
        month = calendar[MONTH].toByte()
        dateOfMonth = calendar[DAY_OF_MONTH].toByte()
        hours = calendar[HOUR_OF_DAY].toByte()
        minutes = calendar[MINUTE].toByte()
        seconds = calendar[SECOND].toByte()
        dateOfWeek = calendar[DAY_OF_WEEK].toByte()
    }

    override fun toByteArray(): ByteArray {
        return byteArrayOf(
                code, yearLastTwoDigits, month, dateOfMonth, hours, minutes, seconds, dateOfWeek
        )
    }

    override fun getMainContent(): String {
        return "$hours:$minutes:$seconds"
    }

}