package com.happs.ximand.ringcontrol.model.mapper.impl

import android.content.ContentValues
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.Mapper

class TimetableToContentValuesMapper : Mapper<Timetable, ContentValues> {

    override fun map(from: Timetable): ContentValues {
        val timetableBuilder = StringBuilder()
        for (lesson in from.lessons) {
            timetableBuilder
                    .append(lesson.startTime.toString())
                    .append(";")
                    .append(lesson.endTime.toString())
                    .append(";")
        }
        val title: String = from.title
        val timetable = timetableBuilder
                .substring(0, timetableBuilder.lastIndexOf(";"))
        val contentValues = ContentValues()
        contentValues.put(TimetableDatabaseHelper.DB_FIELD_TITLE, title)
        contentValues.put(TimetableDatabaseHelper.DB_FIELD_TIMETABLE, timetable)
        return contentValues
    }

}