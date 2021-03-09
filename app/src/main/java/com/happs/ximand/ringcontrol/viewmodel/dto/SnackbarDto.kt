package com.happs.ximand.ringcontrol.viewmodel.dto

import android.view.View

class SnackbarDto(val messageResId: Int, val duration: Int) {

    var iconResId = 0
        private set
    var actionResId = ACTION_NONE
        private set
    var actionClickListener: ((view: View) -> Unit)? = null
        private set

    companion object {
        const val ACTION_NONE = 0
        const val ICON_NONE = 0
    }

    fun setIconResId(iconResId: Int): SnackbarDto {
        this.iconResId = iconResId
        return this
    }

    fun setActionResId(actionResId: Int): SnackbarDto {
        this.actionResId = actionResId
        return this
    }

    fun setActionClickListener(onActionClick: (view: View) -> Unit): SnackbarDto {
        this.actionClickListener = onActionClick
        return this
    }

}