package com.happs.ximand.ringcontrol.model.bl.thread

import com.happs.ximand.ringcontrol.model.bl.callback.ReceiveCallback
import java.io.IOException
import java.io.InputStream

class ReceiveThread(private val inputStream: InputStream) : Thread() {

    private var receiveCallback: ReceiveCallback? = null

    override fun run() {
        super.run()
        while (true) {
            try {
                if (inputStream.available() > 0) {
                    val bytes = ByteArray(inputStream.available())
                    for (i in 0..inputStream.available()) {
                        bytes[i] = inputStream.read().toByte()
                    }
                    receiveCallback?.onReceive(bytes)
                }
                sleep(100)
            } catch (e: InterruptedException) {
                break
            } catch (e: IOException) {
                receiveCallback?.onException(e)
            }
        }
    }
}