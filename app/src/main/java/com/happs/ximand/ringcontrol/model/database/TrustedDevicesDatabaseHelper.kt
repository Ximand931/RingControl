package com.happs.ximand.ringcontrol.model.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TrustedDevicesDatabaseHelper(context: Context?, version: Int)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, version) {

    companion object {
        const val DATABASE_NAME = "TRUSTED_DEVICES"
        const val DB_FIELD_ID = "ID"
        const val DB_FIELD_MANUFACTURER = "MANUFACTURER"
        const val DB_FIELD_MODEL = "MODEL"
        const val DB_FIELD_DATE = "DATE"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + "("
                + DB_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DB_FIELD_MANUFACTURER + " TEXT, "
                + DB_FIELD_MODEL + " TEXT, "
                + DB_FIELD_DATE + " TEXT"
                + ");")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DATABASE_NAME")
    }
}