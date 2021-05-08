package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class ReceiveDataException(cause: Throwable) : BaseException(CODE, cause) {

    companion object {
        const val CODE = 0x25
    }

    override val descriptionResId: Int = R.string.error_while_receive_data
    override val showSupportLayout = true

}