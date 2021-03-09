package com.happs.ximand.ringcontrol.model.`object`.timetable

import java.io.Serializable
import java.util.*

class Timetable : Serializable {
    var id = 0
    var title: String
    var lessons: List<Lesson>

    constructor(title: String, lessons: List<Lesson>) {
        this.title = title
        this.lessons = lessons
    }

    constructor(id: Int, title: String, lessons: List<Lesson>) {
        this.id = id
        this.title = title
        this.lessons = lessons
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val timetable = other as Timetable
        return id == timetable.id &&
                title == timetable.title &&
                lessons == timetable.lessons
    }

    override fun hashCode(): Int {
        return Objects.hash(id, title, lessons)
    }

    override fun toString(): String {
        return "Timetable{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lessons=" + lessons +
                '}'
    }
}