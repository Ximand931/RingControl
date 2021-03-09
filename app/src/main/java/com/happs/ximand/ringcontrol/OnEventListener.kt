package com.happs.ximand.ringcontrol

@Deprecated("Use kotlin syntax")
@FunctionalInterface
interface OnEventListener<T> {
    fun onEvent(t: T)
}