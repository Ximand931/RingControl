package com.happs.ximand.ringcontrol.model.`object`.exception

import com.happs.ximand.ringcontrol.R

class WhileConnectingException(cause: Throwable) : BaseException(CODE, cause) {

    companion object {
        const val CODE = 0x11
    }

    override val descriptionResId: Int = R.string.error_while_connecting_desc
    override val showRestartLayout: Boolean = true
    override val showSupportLayout: Boolean = true

}