package com.happs.ximand.ringcontrol.view

import android.text.Editable
import android.text.TextWatcher

class MaskWatcher(var mask: String) : TextWatcher {

    companion object {
        private const val INPUT_CHAR = 's'
    }

    private var adding = false
    private var lineFilledListener: OnLineFilledListener? = null

    fun setLineFilledListener(lineFilledListener: OnLineFilledListener?) {
        this.lineFilledListener = lineFilledListener
    }

    override fun afterTextChanged(editable: Editable) {
        val length = editable.length
        if (mask.length > length) {
            val currentMaskChar = mask[length]
            if (adding) {
                if (currentMaskChar != INPUT_CHAR) {
                    editable.append(currentMaskChar)
                }
            } else {
                if (currentMaskChar != INPUT_CHAR) {
                    editable.delete(editable.length - 1, editable.length)
                }
            }
        } else {
            lineFilledListener!!.onFilled()
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        adding = after > count
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}

    @FunctionalInterface
    interface OnLineFilledListener {
        fun onFilled()
    }

}