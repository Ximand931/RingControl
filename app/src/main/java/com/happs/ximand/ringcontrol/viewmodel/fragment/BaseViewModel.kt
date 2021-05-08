package com.happs.ximand.ringcontrol.viewmodel.fragment

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException
import com.happs.ximand.ringcontrol.view.fragment.ExceptionFragment
import com.happs.ximand.ringcontrol.viewmodel.dto.SnackbarDto

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    val makeSnackbarEvent = SingleLiveEvent<SnackbarDto>()
    val permissionRequest = MutableLiveData<String>()

    fun makeExceptionSnackbarWithAction(titleResId: Int, e: BaseException) {
        val snackbarDto = SnackbarDto(titleResId, Snackbar.LENGTH_LONG)
        snackbarDto.setActionResId(R.string.details)
                .setActionClickListener {
                    FragmentNavigation.instance
                            .navigateTo(ExceptionFragment.newInstance(e))
                }
        makeSnackbarEvent.value = snackbarDto
    }

    fun makeSimpleSnackbar(titleResId: Int) {
        makeSnackbarEvent.value = SnackbarDto(titleResId, Snackbar.LENGTH_SHORT)
    }

    open fun notifyOptionsMenuItemClicked(itemId: Int): Boolean {
        return false
    }

    open fun onPermissionResult(permission: String, granted: Boolean) {
    }
}