package com.happs.ximand.ringcontrol;

import android.app.Application;

import com.happs.ximand.ringcontrol.model.repository.impl.FakeTimetableRepository;

public class RingControlApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FakeTimetableRepository.initialize();
    }

}
