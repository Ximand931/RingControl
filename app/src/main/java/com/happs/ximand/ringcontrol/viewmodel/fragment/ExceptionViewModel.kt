package com.happs.ximand.ringcontrol.viewmodel.fragment

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException
import com.happs.ximand.ringcontrol.model.`object`.exception.NoOneEmailClientException

class ExceptionViewModel : BaseViewModel() {

    companion object {
        const val SUPPORT_EMAIL = "ximand931@gmail.com"
    }

    val navigateToPermissionSettingsLiveEvent = SingleLiveEvent<Void>()
    val navigateToSupportLiveEvent = SingleLiveEvent<String>()
    val restartApplicationLiveEvent = SingleLiveEvent<Void>()

    var exception: BaseException? = null

    fun attachException(exception: BaseException) {
        this.exception = exception
    }

    fun restartApplication() {
        restartApplicationLiveEvent.call()
    }

    fun navigateToPermissionsSettings() {
        navigateToPermissionSettingsLiveEvent.call()
    }

    fun navigateToSupport() {
        navigateToSupportLiveEvent.value = SUPPORT_EMAIL
    }

    fun onNoOneEmailClient() {
        makeExceptionSnackbarWithAction(R.string.no_one_client_title, NoOneEmailClientException())
    }

}