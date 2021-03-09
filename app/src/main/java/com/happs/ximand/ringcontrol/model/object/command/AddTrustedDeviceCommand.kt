package com.happs.ximand.ringcontrol.model.`object`.command


class AddTrustedDeviceCommand : BluetoothCommand<String?>(COMMAND_CODE) {
    override fun toByteArray(): ByteArray? {
        return null
    }

    override fun getMainContent(): String {
        throw UnsupportedOperationException()
    }

    companion object {
        private const val COMMAND_CODE: Byte = 1
    }
}