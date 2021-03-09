package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class BluetoothIsDisabledException : BaseException(CODE) {

    companion object {
        const val CODE = 0x20
    }

    override val descriptionResId: Int = R.string.bluetooth_is_disabled

}