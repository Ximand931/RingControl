package com.happs.ximand.ringcontrol.viewmodel.util;

import android.animation.ObjectAnimator;
import android.view.View;

public final class AnimationUtils {

    static final int ALPHA_DURATION = 300;
    private static final float INACTIVE_VIEW_ALPHA = 0.5f;

    private AnimationUtils() {
    }

    static void activeAnimation(View view) {
        alphaAnimation(view, INACTIVE_VIEW_ALPHA, 1f);
    }

    static void inactiveAnimation(View view) {
        alphaAnimation(view, 1f, INACTIVE_VIEW_ALPHA);
    }

    private static void alphaAnimation(View view, float... values) {
        ObjectAnimator
                .ofFloat(view, View.ALPHA, values)
                .setDuration(ALPHA_DURATION)
                .start();
    }

/*
const val DURATION = 300L;

    fun appearanceAnimation(view: View) {
        alphaAnimation(view, 0f, 1f)
    }

    fun disappearanceAnimation(view: View) {
        alphaAnimation(view, 1f, 0f)
    }

    private fun alphaAnimation(view: View, vararg values: Float) {
        ObjectAnimator
            .ofFloat(view, View.ALPHA, *values)
            .setDuration(DURATION)
            .start()
    }
 */

}
