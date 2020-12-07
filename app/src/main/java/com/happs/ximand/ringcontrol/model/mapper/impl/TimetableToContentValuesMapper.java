package com.happs.ximand.ringcontrol.model.mapper.impl;

import android.content.ContentValues;

import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper;
import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Timetable;

public class TimetableToContentValuesMapper implements Mapper<Timetable, ContentValues> {

    @Override
    public ContentValues map(Timetable from) {
        final StringBuilder timetableBuilder = new StringBuilder();

        for (Lesson lesson : from.getLessons()) {
            timetableBuilder
                    .append(lesson.getStartTimeDep())
                    .append(";")
                    .append(lesson.getEndTimeDep())
                    .append(";");
        }

        final String title = from.getTitle();
        final String timetable = timetableBuilder
                .substring(0, timetableBuilder.lastIndexOf(";"));

        final ContentValues contentValues = new ContentValues();
        contentValues.put(TimetableDatabaseHelper.DB_FIELD_TITLE, title);
        contentValues.put(TimetableDatabaseHelper.DB_FIELD_TIMETABLE, timetable);

        return contentValues;
    }

}
