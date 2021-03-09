package com.happs.ximand.ringcontrol.view

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.happs.ximand.ringcontrol.R

class SnackbarBuilder(view: View?) {

    val snackbar: Snackbar = Snackbar.make(view!!, R.string.stub, Snackbar.LENGTH_SHORT)

    fun setText(textResId: Int): SnackbarBuilder {
        snackbar.setText(textResId)
        return this
    }

    fun setDuration(duration: Int): SnackbarBuilder {
        snackbar.duration = duration
        return this
    }

    fun setAction(actionResId: Int, clickListener: (view: View) -> Unit): SnackbarBuilder {
        snackbar.setAction(actionResId, clickListener)
        return this
    }

    fun setFormatText(formatStringResId: Int, vararg args: Any?): SnackbarBuilder {
        val res = snackbar.context.resources
        val textFormat = res.getString(formatStringResId)
        snackbar.setText(String.format(textFormat, *args))
        return this
    }

    fun setIcon(iconResId: Int): SnackbarBuilder {
        val snackbarLayout = snackbar.view
        val textView = snackbarLayout
                .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0)
        val res = snackbar.context.resources
        textView.compoundDrawablePadding = res.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        return this
    }

}