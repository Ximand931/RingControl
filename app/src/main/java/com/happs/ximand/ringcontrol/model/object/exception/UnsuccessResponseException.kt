package com.happs.ximand.ringcontrol.model.`object`.exception

import com.happs.ximand.ringcontrol.R

class UnsuccessResponseException(code: Int) : BaseException(code) {

    override val descriptionResId = R.string.unsuccessfully_response_description

    override val showSupportLayout = true

}