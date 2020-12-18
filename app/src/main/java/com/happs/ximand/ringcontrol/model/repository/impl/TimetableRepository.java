package com.happs.ximand.ringcontrol.model.repository.impl;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.happs.ximand.ringcontrol.BuildConfig;
import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper;
import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.mapper.impl.CursorToTimetableMapper;
import com.happs.ximand.ringcontrol.model.mapper.impl.TimetableToContentValuesMapper;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;

public final class TimetableRepository extends BaseRepository<Timetable> {

    private static TimetableRepository instance;

    public static void initialize(Application application) {
        instance = new TimetableRepository(application);
    }

    public static TimetableRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TimetableRepository was not initialized");
        }
        return instance;
    }

    public TimetableRepository(Application application) {
        super(application);
    }

    @Override
    protected SQLiteOpenHelper getSQLiteHelper(Application application) {
        return new TimetableDatabaseHelper(application, BuildConfig.VERSION_CODE);
    }

    @Override
    protected Mapper<Cursor, Timetable> getFromCursorMapper() {
        return new CursorToTimetableMapper();
    }

    @Override
    protected Mapper<Timetable, ContentValues> getToContentValuesMapper() {
        return new TimetableToContentValuesMapper();
    }

    @Override
    protected int getItemId(Timetable item) {
        return item.getId();
    }

    @Override
    protected void setItemId(Timetable item, int id) {
        item.setId(id);
    }
}
