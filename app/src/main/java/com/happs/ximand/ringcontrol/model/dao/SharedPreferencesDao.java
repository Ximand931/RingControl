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

    private static final String PREF_TARGET_DEVICE_ADDRESS = "TARGET_DEVICE";

    private static final String PREF_APPLIED_TIMETABLE = "APPLIED_TIMETABLE_ID";
    private static final int APPLIED_TIMETABLE_DEF_VALUE = 1;

    private static final String PREF_RING_DURATION = "RING_DURATION";
    private static final int RING_DURATION_DEF_VALUE = 3000;

    private static final String PREF_WEEKEND_MODE = "WEEKEND_MODE";
    private static final int WEEKEND_MODE_DEF_VALUE = 0;

    private static final String PREF_MANUAL_MODE_STATE = "MANUAL_MODE";
    private static final boolean MANUAL_MODE_STATE_DEF_VALUE = false;

    private final SharedPreferences preferences;

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

    public String getTargetDeviceAddress() {
        return preferences.getString(PREF_TARGET_DEVICE_ADDRESS, null);
    }

    public void updateTargetDeviceAddress(String address) {
        preferences.edit()
                .putString(PREF_TARGET_DEVICE_ADDRESS, address)
                .apply();
    }

    public int getRingDuration() {
        return preferences.getInt(PREF_RING_DURATION, RING_DURATION_DEF_VALUE);
    }

    public void updateRingDuration(int duration) {
        preferences.edit()
                .putInt(PREF_RING_DURATION, duration)
                .apply();
    }

    public int getWeekendMode() {
        return preferences.getInt(PREF_WEEKEND_MODE, WEEKEND_MODE_DEF_VALUE);
    }

    public void updateWeekendMode(int mode) {
        preferences.edit()
                .putInt(PREF_WEEKEND_MODE, mode)
                .apply();
    }

    public boolean getManualModeState() {
        return preferences.getBoolean(PREF_MANUAL_MODE_STATE, MANUAL_MODE_STATE_DEF_VALUE);
    }

    public void updateManualMode(boolean state) {
        preferences.edit()
                .putBoolean(PREF_MANUAL_MODE_STATE, state)
                .apply();
    }
}
