package com.happs.ximand.ringcontrol

import android.app.Application
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository

class RingControlApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TimetableRepository.initialize(this)
        SharedPreferencesDao.initialize(this)
    }

}