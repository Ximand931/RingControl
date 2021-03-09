package com.happs.ximand.ringcontrol.viewmodel.util

import android.annotation.SuppressLint
import com.happs.ximand.ringcontrol.model.`object`.timetable.Time
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    const val SIMPLE_TIME_PATTERN = "(?:[01]\\d|2[0123]):(?:[012345]\\d) – " +
            "(?:[01]\\d|2[0123]):(?:[012345]\\d)"
    const val DETAILED_TIME_PATTERN = "(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d) " +
            "– (?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)"

    const val SIMPLE_TIME_MASK = "ss:ss – ss:ss"
    const val DETAILED_TIME_MASK = "ss:ss:ss – ss:ss:ss"

    private const val DATE_TIME_PATTERN = "HH:mm:ss"

    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimeWithFewMinutes(minutes: Int): Time {
        val format = SimpleDateFormat(DATE_TIME_PATTERN)
        val currentDateInMillis = Date().time
        val dateAfterFewMinutes = Date(currentDateInMillis + minutes * 30 * 1000)
        return Time(format.format(dateAfterFewMinutes))
    }
}