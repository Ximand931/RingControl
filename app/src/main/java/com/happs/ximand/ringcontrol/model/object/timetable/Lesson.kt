package com.happs.ximand.ringcontrol.model.`object`.timetable

import com.happs.ximand.ringcontrol.model.`object`.timetable.Time.Companion.midnightTime

class Lesson {

    var number: Int
    private var lessonScope: LessonScope

    constructor(number: Int) {
        this.number = number
        lessonScope = LessonScope(midnightTime, midnightTime)
    }

    constructor(number: Int, startTime: Time, endTime: Time) {
        this.number = number
        lessonScope = LessonScope(startTime, endTime)
    }

    var startTime: Time
        get() = lessonScope.startTime
        set(startTime) {
            lessonScope.startTime = startTime
        }

    var endTime: Time
        get() = lessonScope.endTime
        set(endTime) {
            lessonScope.endTime = endTime
        }

}