package com.happs.ximand.ringcontrol.model.`object`.response

class Response(val commandCode: Byte, val responseCode: Byte) {

    val isSuccess: Boolean
        get() = responseCode == RESPONSE_OK

    companion object {
        const val RESPONSE_OK = 0xff.toByte()

        @JvmStatic
        fun getResponseByMessage(message: ByteArray): Response {
            return if (message.size == 2) {
                Response(message[0], message[1])
            } else empty()
        }

        fun empty(): Response {
            return Response(0.toByte(), 0.toByte())
        }
    }

}