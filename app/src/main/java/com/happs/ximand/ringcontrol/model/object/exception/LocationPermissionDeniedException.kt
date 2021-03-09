package com.happs.ximand.ringcontrol.model.`object`.exception

import com.happs.ximand.ringcontrol.R

class LocationPermissionDeniedException : BaseException(CODE) {

    companion object {
        const val CODE = 0x10
    }

    override val descriptionResId: Int = R.string.location_perm_deny_description
}