package com.happs.ximand.ringcontrol.model.bl.callback

interface SendCallback : Callback {
    fun onSent(sentBytes: ByteArray)
}