package com.happs.ximand.ringcontrol.viewmodel.dto

class SelectAlertDialogDto {

    var titleResId = 0
        private set
    var itemsArrayResId = 0
        private set
    var selectListener: ((position: Int) -> Unit)? = null
        private set
    var cancelListener: (() -> Unit)? = null
        private set

    fun setTitleResId(titleResId: Int): SelectAlertDialogDto {
        this.titleResId = titleResId
        return this
    }

    fun setItemsArrayResId(itemsArrayResId: Int): SelectAlertDialogDto {
        this.itemsArrayResId = itemsArrayResId
        return this
    }

    fun setSelectListener(selectListener: ((position: Int) -> Unit)?): SelectAlertDialogDto {
        this.selectListener = selectListener
        return this
    }

    fun setCancelListener(cancelListener: (() -> Unit)?): SelectAlertDialogDto {
        this.cancelListener = cancelListener
        return this
    }
}