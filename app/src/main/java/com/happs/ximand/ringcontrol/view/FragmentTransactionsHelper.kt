package com.happs.ximand.ringcontrol.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.happs.ximand.ringcontrol.R

object FragmentTransactionsHelper {

    private const val CONTAINER_ID = R.id.container_main

    @JvmStatic
    fun replaceFragment(manager: FragmentManager, fragment: Fragment) {
        val tag = fragment.javaClass.simpleName
        manager.beginTransaction()
                .setCustomAnimations(
                        R.animator.fade_in, R.animator.fade_out,
                        R.animator.fade_in, R.animator.fade_out
                )
                .replace(CONTAINER_ID, fragment, )
                .addToBackStack(tag)
                .commitAllowingStateLoss()
    }
}