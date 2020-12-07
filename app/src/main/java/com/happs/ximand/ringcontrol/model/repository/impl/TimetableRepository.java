package com.happs.ximand.ringcontrol.model.repository.impl;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.happs.ximand.ringcontrol.BuildConfig;
import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper;
import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.mapper.impl.CursorToTimetableMapper;
import com.happs.ximand.ringcontrol.model.mapper.impl.TimetableToContentValuesMapper;
import com.happs.ximand.ringcontrol.model.object.Timetable;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.specification.SqlSpecification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimetableRepository implements Repository<Timetable> {

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

    private final SQLiteOpenHelper sqLiteHelper;

    private final Mapper<Cursor, Timetable> cursorToTimetableMapper;
    private final Mapper<Timetable, ContentValues> timetableToContentValuesMapper;

    private TimetableRepository(Application application) {
        this.sqLiteHelper = new TimetableDatabaseHelper(application, BuildConfig.VERSION_CODE);
        this.cursorToTimetableMapper = new CursorToTimetableMapper();
        this.timetableToContentValuesMapper = new TimetableToContentValuesMapper();
    }

    @Override
    public void add(Timetable item) {
        add(Collections.singletonList(item));
    }

    @Override
    public void add(Iterable<Timetable> items) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            for (Timetable item : items) {
                final ContentValues contentValues = timetableToContentValuesMapper.map(item);
                final int id = (int) database.insert(TimetableDatabaseHelper.DATABASE_NAME,
                        null, contentValues);
                item.setId(id);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void update(Timetable item) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            final ContentValues contentValues = timetableToContentValuesMapper.map(item);
            database.update(TimetableDatabaseHelper.DATABASE_NAME, contentValues,
                    TimetableDatabaseHelper.DB_FIELD_ID + " = " + item.getId(), null
            );
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void remove(Timetable item) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            database.execSQL("DELETE FROM " + TimetableDatabaseHelper.DATABASE_NAME
                    + " WHERE " + TimetableDatabaseHelper.DB_FIELD_ID + " = " + item.getId());
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void remove(SqlSpecification sqlSpecification) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            database.execSQL("DELETE FROM " + TimetableDatabaseHelper.DATABASE_NAME
                    + " WHERE " + sqlSpecification.toSqlClauses());
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public List<Timetable> query(SqlSpecification sqlSpecification) {
        final SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        final List<Timetable> timetables = new ArrayList<>();

        try {
            database.beginTransaction();
            final Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + TimetableDatabaseHelper.DATABASE_NAME +
                            " " + sqlSpecification.toSqlClauses(), new String[]{}
            );

            if (cursor.moveToFirst()) {
                do {
                    timetables.add(cursorToTimetableMapper.map(cursor));
                } while (cursor.moveToNext());
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }

        return timetables;
    }

}
