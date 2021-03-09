package com.happs.ximand.ringcontrol.viewmodel.util

import android.animation.ObjectAnimator
import android.view.View

object AnimationUtils {

    const val ALPHA_DURATION = 300
    private const val INACTIVE_VIEW_ALPHA = 0.5f

    fun activeAnimation(view: View) {
        alphaAnimation(view, INACTIVE_VIEW_ALPHA, 1f)
    }

    fun inactiveAnimation(view: View) {
        alphaAnimation(view, 1f, INACTIVE_VIEW_ALPHA)
    }

    private fun alphaAnimation(view: View, vararg values: Float) {
        ObjectAnimator
                .ofFloat(view, View.ALPHA, *values)
                .setDuration(ALPHA_DURATION.toLong())
                .start()
    }
}