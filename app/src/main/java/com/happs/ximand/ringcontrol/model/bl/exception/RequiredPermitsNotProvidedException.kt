package com.happs.ximand.ringcontrol.model.bl.exception

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException

class RequiredPermitsNotProvidedException : BaseException(CODE) {

    companion object {
        const val CODE = 0x10
    }

    override val descriptionResId = R.string.location_perm_deny_description
    override val showRestartLayout = true
    override val showPermissionsLayout = true

}