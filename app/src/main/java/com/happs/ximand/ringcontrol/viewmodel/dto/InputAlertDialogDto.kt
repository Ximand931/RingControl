package com.happs.ximand.ringcontrol.viewmodel.dto

class InputAlertDialogDto {

    var titleResId = 0
        private set
    var inputCompleteListener: ((input: String) -> Unit)? = null
        private set
    var cancelListener: (() -> Unit)? = null
        private set

    fun setTitleResId(titleResId: Int): InputAlertDialogDto {
        this.titleResId = titleResId
        return this
    }

    fun setInputCompleteListener(onInput: ((input: String) -> Unit)?): InputAlertDialogDto {
        this.inputCompleteListener = onInput
        return this
    }

    fun setCancelListener(onCancel: (() -> Unit)?): InputAlertDialogDto {
        this.cancelListener = onCancel
        return this
    }
}