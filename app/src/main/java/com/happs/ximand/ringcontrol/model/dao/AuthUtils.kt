package com.happs.ximand.ringcontrol.model.dao

import android.os.Build
import java.util.*

object AuthUtils {

    val UUID = UUID(getUniqueDeviceId().hashCode().toLong(), Build.getRadioVersion().hashCode().toLong()).toString()

    private fun getUniqueDeviceId(): String {
        return "35" + Build.BOARD.length % 10 +
                Build.BRAND.length % 10 +
                Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 +
                Build.HOST.length % 10 +
                Build.ID.length % 10 +
                Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 +
                Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 +
                Build.TYPE.length % 10 +
                Build.USER.length % 10
    }
}