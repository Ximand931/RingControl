package com.happs.ximand.ringcontrol.model.mapper.impl;

import android.database.Cursor;

import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper;
import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Time;
import com.happs.ximand.ringcontrol.model.object.Timetable;

import java.util.ArrayList;
import java.util.List;

public class CursorToTimetableMapper implements Mapper<Cursor, Timetable> {

    @Override
    public Timetable map(Cursor from) {
        int idColumnIndex = from.getColumnIndex(TimetableDatabaseHelper.DB_FIELD_ID);
        int titleColumnIndex = from.getColumnIndex(TimetableDatabaseHelper.DB_FIELD_TITLE);
        int timetableColumnIndex = from.getColumnIndex(TimetableDatabaseHelper.DB_FIELD_TIMETABLE);

        final int id = from.getInt(idColumnIndex);
        final String title = from.getString(titleColumnIndex);

        List<Lesson> lessons = new ArrayList<>();
        String[] timeArray = from.getString(timetableColumnIndex).split(";");
        for (int i = 0; i < timeArray.length; i += 2) {
            int num = i / 2 + 1;
            Lesson lesson = new Lesson(num, new Time(timeArray[i]), new Time(timeArray[i + 1]));
            lessons.add(lesson);
        }

        return new Timetable(id, title, lessons);
    }

}
