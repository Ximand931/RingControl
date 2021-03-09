package com.happs.ximand.ringcontrol.model.`object`.command

class AuthenticationCommand : BluetoothCommand<String?>(COMMAND_CODE) {
    override fun toByteArray(): ByteArray? {
        //return getCode() + ;
        return null
    }

    override fun getMainContent(): String {
        throw UnsupportedOperationException()
    }

    companion object {
        private const val COMMAND_CODE: Byte = 0
    }
}