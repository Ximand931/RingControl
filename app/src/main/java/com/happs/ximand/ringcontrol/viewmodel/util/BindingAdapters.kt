package com.happs.ximand.ringcontrol.viewmodel.util

import android.os.Handler
import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {

    private const val ERROR_MESSAGE_SHOW_DURATION = 5000

    @JvmStatic
    @BindingAdapter("constantErrorText")
    fun setConstantErrorText(layout: TextInputLayout, error: String?) {
        setErrorText(layout, error)
    }

    @JvmStatic
    @BindingAdapter("temporaryErrorText")
    fun setTemporaryErrorText(layout: TextInputLayout,
                              error: String?) {
        val success = setErrorText(layout, error)
        if (success) {
            Handler().postDelayed({ layout.error = null }, ERROR_MESSAGE_SHOW_DURATION.toLong())
        }
    }

    private fun setErrorText(layout: TextInputLayout, error: String?): Boolean {
        if (layout.error == null && error == null) {
            return false
        }
        layout.error = error
        return true
    }

    @JvmStatic
    @BindingAdapter("inactiveOnEvent")
    fun inactiveOnEvent(view: View, event: Boolean?) {
        if (event!!) {
            view.isEnabled = false
            AnimationUtils.inactiveAnimation(view)
        } else {
            AnimationUtils.activeAnimation(view)
            Handler().postDelayed(
                    { view.isEnabled = true }, AnimationUtils.ALPHA_DURATION
                    .toLong())
        }
    }
}