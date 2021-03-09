package com.happs.ximand.ringcontrol.model.mapper.impl

import android.database.Cursor
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Time
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.Mapper
import java.util.*

class CursorToTimetableMapper : Mapper<Cursor, Timetable> {
    override fun map(from: Cursor): Timetable {
        val idColumnIndex = from.getColumnIndex(TimetableDatabaseHelper.DB_FIELD_ID)
        val titleColumnIndex = from.getColumnIndex(TimetableDatabaseHelper.DB_FIELD_TITLE)
        val timetableColumnIndex = from.getColumnIndex(TimetableDatabaseHelper.DB_FIELD_TIMETABLE)
        val id = from.getInt(idColumnIndex)
        val title = from.getString(titleColumnIndex)
        val lessons: MutableList<Lesson> = ArrayList()
        val timeArray = from.getString(timetableColumnIndex).split(";").toTypedArray()
        var i = 0
        while (i < timeArray.size) {
            val num = i / 2 + 1
            val lesson = Lesson(num, Time(timeArray[i]), Time(timeArray[i + 1]))
            lessons.add(lesson)
            i += 2
        }
        return Timetable(id, title, lessons)
    }
}