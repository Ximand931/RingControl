package com.happs.ximand.ringcontrol.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment
import com.happs.ximand.ringcontrol.view.fragment.SelectDeviceFragment

class MainActivityViewModel : ViewModel(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        FragmentNavigation.instance.navigateTo(getSuitableFragment())
    }

    private fun getSuitableFragment(): Fragment {
        return if (SharedPreferencesDao.instance.getTargetDeviceAddress() != null) {
            AllTimetablesFragment.newInstance()
        } else {
            SelectDeviceFragment.newInstance()
        }
    }

}