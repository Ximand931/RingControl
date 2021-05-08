package com.happs.ximand.ringcontrol.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ActivityMainBinding
import com.happs.ximand.ringcontrol.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

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
        initViewModel()
        initBinding()
        initToolbar()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(MainActivityViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
    }

    private fun initToolbar() {
        val toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)
    }
}