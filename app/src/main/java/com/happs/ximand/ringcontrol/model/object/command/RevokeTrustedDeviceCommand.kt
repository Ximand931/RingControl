package com.happs.ximand.ringcontrol.model.`object`.command

class RevokeTrustedDeviceCommand : BluetoothCommand<String?>(REVOKE_TRUSTED_DEVICE_COMMAND_CODE) {
    override fun toByteArray(): ByteArray {
        return ByteArray(0)
    }

    override fun getMainContent(): String {
        throw UnsupportedOperationException()
    }

    companion object {
        private const val REVOKE_TRUSTED_DEVICE_COMMAND_CODE: Byte = 2
    }
}