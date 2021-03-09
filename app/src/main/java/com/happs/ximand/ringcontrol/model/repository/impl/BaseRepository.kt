package com.happs.ximand.ringcontrol.model.repository.impl

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.Mapper
import com.happs.ximand.ringcontrol.model.repository.Repository
import com.happs.ximand.ringcontrol.model.specification.SqlSpecification
import java.util.*

abstract class BaseRepository<T> : Repository<T> {

    protected abstract val sqLiteHelper: SQLiteOpenHelper
    protected abstract val fromCursorMapper: Mapper<Cursor, T>
    protected abstract val toContentValuesMapper: Mapper<T, ContentValues>

    override fun add(item: T) {
        val database = sqLiteHelper.writableDatabase
        try {
            database.beginTransaction()
            val contentValues = toContentValuesMapper.map(item)
            val id = database.insert(TimetableDatabaseHelper.DATABASE_NAME,
                    null, contentValues).toInt()
            setItemId(item, id)
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            database.close()
        }
    }

    override fun update(item: T) {
        val database = sqLiteHelper.writableDatabase
        try {
            database.beginTransaction()
            val contentValues = toContentValuesMapper.map(item)
            database.update(TimetableDatabaseHelper.DATABASE_NAME, contentValues,
                    TimetableDatabaseHelper.DB_FIELD_ID + " = " + getItemId(item),
                    null
            )
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            database.close()
        }
    }

    override fun remove(item: T) {
        val database = sqLiteHelper.writableDatabase
        try {
            database.beginTransaction()
            database.execSQL("DELETE FROM " + TimetableDatabaseHelper.DATABASE_NAME
                    + " WHERE " + TimetableDatabaseHelper.DB_FIELD_ID + " = " + getItemId(item))
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            database.close()
        }
    }

    override fun query(sqlSpecification: SqlSpecification): MutableList<T> {
        val database = sqLiteHelper.readableDatabase
        val items: MutableList<T> = ArrayList()
        try {
            database.beginTransaction()
            val cursor = database.rawQuery(
                    "SELECT * FROM " + TimetableDatabaseHelper.DATABASE_NAME +
                            " " + sqlSpecification.toSqlClauses(), arrayOf())
            if (cursor.moveToFirst()) {
                do {
                    items.add(fromCursorMapper.map(cursor))
                } while (cursor.moveToNext())
            }
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            database.close()
        }
        return items
    }

    protected abstract fun getItemId(item: T): Int

    protected abstract fun setItemId(item: T, id: Int)
}