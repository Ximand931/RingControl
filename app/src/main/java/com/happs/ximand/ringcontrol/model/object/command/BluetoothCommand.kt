package com.happs.ximand.ringcontrol.model.`object`.command

abstract class BluetoothCommand<T>(protected val code: Byte) {

    abstract fun toByteArray(): ByteArray?
    abstract fun getMainContent(): T

}