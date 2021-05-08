package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class SendDataException(cause: Throwable) : BaseException(CODE, cause) {

    companion object {
        const val CODE = 0x23
    }

    override val descriptionResId: Int = com.happs.ximand.ringcontrol.R.string.error_while_send_data //TODO: STUB
    override val showRestartLayout = true
    override val showSupportLayout = true

}