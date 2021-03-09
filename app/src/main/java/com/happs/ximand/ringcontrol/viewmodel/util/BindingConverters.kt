package com.happs.ximand.ringcontrol.viewmodel.util

import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Time

object BindingConverters {

    @JvmStatic
    fun convertTimeToPreviewTimeString(time: Time): String {
        val simplifiedTimeBuilder = StringBuilder()
        simplifiedTimeBuilder.append(time.hours)
        simplifiedTimeBuilder.append(":")
        if (time.minutes < 10) {
            simplifiedTimeBuilder.append("0")
        }
        simplifiedTimeBuilder.append(time.minutes)
        if (time.seconds != 0) {
            simplifiedTimeBuilder.append(":")
            if (time.seconds < 10) {
                simplifiedTimeBuilder.append("0")
            }
            simplifiedTimeBuilder.append(time.seconds)
        }
        return simplifiedTimeBuilder.toString()
    }

    @JvmStatic
    fun convertListToLessonsDetails(lessons: List<Lesson>): String {
        val previewBuilder = StringBuilder()
        for (lesson in lessons) {
            if (previewBuilder.length > 86) {
                break
            }
            previewBuilder
                    .append(convertTimeToPreviewTimeString(lesson.startTime))
                    .append(" - ")
                    .append(convertTimeToPreviewTimeString(lesson.endTime))
                    .append(", ")
        }
        return prunePreviewString(previewBuilder.toString())
    }

    private fun prunePreviewString(s: String): String {
        return if (s.length > 86) {
            s.substring(
                    0, s.substring(0, 86).lastIndexOf(',')
            ) + "..."
        } else {
            s.substring(0, s.length - 2)
        }
    }
}