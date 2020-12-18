package com.happs.ximand.ringcontrol.model.repository.impl;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper;
import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.repository.Repository;
import com.happs.ximand.ringcontrol.model.specification.SqlSpecification;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T> implements Repository<T> {

    private final SQLiteOpenHelper sqLiteHelper;
    private final Mapper<Cursor, T> cursorTMapper;
    private final Mapper<T, ContentValues> tContentValuesMapper;

    public BaseRepository(Application application) {
        this.sqLiteHelper = getSQLiteHelper(application);
        this.cursorTMapper = getFromCursorMapper();
        this.tContentValuesMapper = getToContentValuesMapper();
    }

    protected abstract SQLiteOpenHelper getSQLiteHelper(Application application);

    protected abstract Mapper<Cursor, T> getFromCursorMapper();

    protected abstract Mapper<T, ContentValues> getToContentValuesMapper();

    @Override
    public void add(T item) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            final ContentValues contentValues = tContentValuesMapper.map(item);
            final int id = (int) database.insert(TimetableDatabaseHelper.DATABASE_NAME,
                    null, contentValues);
            setItemId(item, id);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void update(T item) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            final ContentValues contentValues = tContentValuesMapper.map(item);
            database.update(TimetableDatabaseHelper.DATABASE_NAME, contentValues,
                    TimetableDatabaseHelper.DB_FIELD_ID + " = " + getItemId(item),
                    null
            );
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void remove(T item) {
        final SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        try {
            database.beginTransaction();
            database.execSQL("DELETE FROM " + TimetableDatabaseHelper.DATABASE_NAME
                    + " WHERE " + TimetableDatabaseHelper.DB_FIELD_ID + " = " + getItemId(item));
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public List<T> query(SqlSpecification sqlSpecification) {
        final SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        final List<T> items = new ArrayList<>();
        try {
            database.beginTransaction();
            final Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + TimetableDatabaseHelper.DATABASE_NAME +
                            " " + sqlSpecification.toSqlClauses(), new String[]{}
            );
            if (cursor.moveToFirst()) {
                do {
                    items.add(cursorTMapper.map(cursor));
                } while (cursor.moveToNext());
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
        return items;
    }

    protected abstract int getItemId(T item);

    protected abstract void setItemId(T item, int id);

}
