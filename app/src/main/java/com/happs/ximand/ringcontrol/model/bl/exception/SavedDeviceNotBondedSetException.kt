package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class SavedDeviceNotBondedSetException : BaseException(CODE) {

    companion object {
        const val CODE = 0x11
    }

    override val descriptionResId = com.happs.ximand.ringcontrol.R.string.saved_device_not_bonded
    override val showRestartLayout = true
}