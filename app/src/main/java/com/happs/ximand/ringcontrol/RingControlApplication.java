package com.happs.ximand.ringcontrol;

import android.app.Application;

import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao;
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository;

public class RingControlApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TimetableRepository.initialize(this);
        SharedPreferencesDao.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
