package com.happs.ximand.ringcontrol.model.`object`.exception

abstract class BaseException : Exception {

    val code: Int
    abstract val descriptionResId: Int

    open val showSupportLayout = false
    open val showRestartLayout = false
    open val showPermissionsLayout = false

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, cause: Throwable?) : super(cause) {
        this.code = code
    }

}