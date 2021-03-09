package com.happs.ximand.ringcontrol.model.bl.callback

interface ReceiveCallback : Callback {
    fun onReceive(bytes: ByteArray)
}