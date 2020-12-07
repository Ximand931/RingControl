package com.happs.ximand.ringcontrol.model.dao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public final class SharedPreferencesDao {

    private static SharedPreferencesDao instance;

    public static void initialize(Application application) {
        instance = new SharedPreferencesDao(application);
    }

    public static SharedPreferencesDao getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SharedPreferencesDao was not initialized");
        }
        return instance;
    }

    private static final String PREF_NAME = "PREFERENCES";
    private static final String PREF_APPLIED_TIMETABLE = "APPLIED_TIMETABLE_ID";
    private static final int APPLIED_TIMETABLE_DEF_VALUE = 1;

    private SharedPreferences preferences;

    private SharedPreferencesDao(Application application) {
        this.preferences = application
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public int getAppliedTimetableId() {
        return preferences.getInt(PREF_APPLIED_TIMETABLE, APPLIED_TIMETABLE_DEF_VALUE);
    }

    public void updateAppliedTimetableId(int newId) {
        preferences.edit()
                .putInt(PREF_APPLIED_TIMETABLE, newId)
                .apply();
    }
}
