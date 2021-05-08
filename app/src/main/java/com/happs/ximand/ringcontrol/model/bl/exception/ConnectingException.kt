package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class ConnectingException(cause: Throwable) : BaseException(CODE, cause) {

    companion object {
        const val CODE = 0x24
    }

    override val descriptionResId: Int = R.string.error_while_connection
    override val showRestartLayout = true
    override val showSupportLayout = true

}