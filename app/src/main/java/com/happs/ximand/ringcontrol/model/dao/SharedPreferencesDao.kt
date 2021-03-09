package com.happs.ximand.ringcontrol.model.dao

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesDao private constructor(application: Application) {

    private val preferences: SharedPreferences

    companion object {
        private var instance: SharedPreferencesDao? = null
        fun initialize(application: Application) {
            instance = SharedPreferencesDao(application)
        }

        fun getInstance(): SharedPreferencesDao {
            return checkNotNull(instance) { "SharedPreferencesDao was not initialized" }
        }

        private const val PREF_NAME = "PREFERENCES"
        private const val PREF_TARGET_DEVICE_ADDRESS = "TARGET_DEVICE"
        private const val PREF_APPLIED_TIMETABLE = "APPLIED_TIMETABLE_ID"
        private const val APPLIED_TIMETABLE_DEF_VALUE = 1
        private const val PREF_RING_DURATION = "RING_DURATION"
        private const val RING_DURATION_DEF_VALUE = 3000
        private const val PREF_WEEKEND_MODE = "WEEKEND_MODE"
        private const val WEEKEND_MODE_DEF_VALUE = 0
        private const val PREF_MANUAL_MODE_STATE = "MANUAL_MODE"
        private const val MANUAL_MODE_STATE_DEF_VALUE = false
    }

    init {
        preferences = application
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getAppliedTimetableId(): Int {
        return preferences.getInt(PREF_APPLIED_TIMETABLE, APPLIED_TIMETABLE_DEF_VALUE)
    }

    fun updateAppliedTimetableId(newId: Int) {
        preferences.edit()
                .putInt(PREF_APPLIED_TIMETABLE, newId)
                .apply()
    }

    fun getTargetDeviceAddress(): String? {
        return preferences.getString(PREF_TARGET_DEVICE_ADDRESS, null)
    }

    fun updateTargetDeviceAddress(address: String?) {
        preferences.edit()
                .putString(PREF_TARGET_DEVICE_ADDRESS, address)
                .apply()
    }

    fun getRingDuration(): Int {
        return preferences.getInt(PREF_RING_DURATION, RING_DURATION_DEF_VALUE)
    }

    fun updateRingDuration(duration: Int) {
        preferences.edit()
                .putInt(PREF_RING_DURATION, duration)
                .apply()
    }

    fun getWeekendMode(): Int {
        return preferences.getInt(PREF_WEEKEND_MODE, WEEKEND_MODE_DEF_VALUE)
    }

    fun updateWeekendMode(mode: Int) {
        preferences.edit()
                .putInt(PREF_WEEKEND_MODE, mode)
                .apply()
    }

    fun getManualModeState(): Boolean {
        return preferences.getBoolean(PREF_MANUAL_MODE_STATE, MANUAL_MODE_STATE_DEF_VALUE)
    }

    fun updateManualMode(state: Boolean) {
        preferences.edit()
                .putBoolean(PREF_MANUAL_MODE_STATE, state)
                .apply()
    }

}