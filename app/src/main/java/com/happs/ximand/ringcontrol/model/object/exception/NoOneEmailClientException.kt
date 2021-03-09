package com.happs.ximand.ringcontrol.model.`object`.exception

import com.happs.ximand.ringcontrol.R

class NoOneEmailClientException : BaseException(CODE) {

    companion object {
        const val CODE = 0xA0
    }

    override val descriptionResId: Int = R.string.no_one_email_client

}