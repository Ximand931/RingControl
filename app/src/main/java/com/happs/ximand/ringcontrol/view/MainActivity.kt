package com.happs.ximand.ringcontrol.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment

class MainActivity : AppCompatActivity() {

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finishAndRemoveTask()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentNavigation.initialize(supportFragmentManager)
        BluetoothNDao.initialize(this)
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        FragmentNavigation.getInstance().navigateTo(AllTimetablesFragment.newInstance())
    }
}