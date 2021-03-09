package com.happs.ximand.ringcontrol.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ActivityMainBinding
import com.happs.ximand.ringcontrol.model.dao.BluetoothNDao

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
        FragmentNavigation.initialize(supportFragmentManager)
        BluetoothNDao.initialize(this)
        val binding: ActivityMainBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_main)
        val toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
    }
}