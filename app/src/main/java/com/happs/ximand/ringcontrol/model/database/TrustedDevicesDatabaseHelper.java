package com.happs.ximand.ringcontrol.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TrustedDevicesDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TRUSTED_DEVICES";
    public static final String DB_FIELD_ID = "ID";
    public static final String DB_FIELD_MANUFACTURER = "MANUFACTURER";
    public static final String DB_FIELD_MODEL = "MODEL";
    public static final String DB_FIELD_DATE = "DATE";

    public TrustedDevicesDatabaseHelper(@Nullable Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DATABASE_NAME + "("
                + DB_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DB_FIELD_MANUFACTURER + " TEXT, "
                + DB_FIELD_MODEL + " TEXT, "
                + DB_FIELD_DATE + " TEXT"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
    }
}
