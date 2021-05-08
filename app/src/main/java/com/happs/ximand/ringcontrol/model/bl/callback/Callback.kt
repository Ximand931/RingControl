package com.happs.ximand.ringcontrol.model.bl.callback

import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

interface Callback {
    fun onException(e: BaseException)
}