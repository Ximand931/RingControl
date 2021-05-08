package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class PairingDeviceException(cause: Throwable) : BaseException(CODE, cause) {

    companion object {
        const val CODE = 0x22
    }

    override val descriptionResId: Int = com.happs.ximand.ringcontrol.R.string.stub
    override val showSupportLayout = true
    override val showRestartLayout = true
}