package com.happs.ximand.ringcontrol

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.happs.ximand.ringcontrol.view.FragmentTransactionsHelper.replaceFragment
import com.happs.ximand.ringcontrol.view.fragment.BaseFragment
import java.lang.ref.WeakReference

class FragmentNavigation private constructor(manager: FragmentManager?) {

    private val managerRef: WeakReference<FragmentManager>

    fun navigateToPreviousFragment() {
        val manager = managerRef.get()
        manager?.popBackStackImmediate()
    }

    fun navigateTo(fragment: Fragment?) {
        val manager = managerRef.get()
        replaceFragment(checkNotNull(manager), fragment!!)
    }

    fun notifyFragmentAboutEvent(tag: String, eventId: Int) {
        val fragment = findFragmentByTag<BaseFragment<*, *>>(tag)
        checkNotNull(fragment).onExternalEvent(eventId)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : BaseFragment<*, *>?> findFragmentByTag(tag: String): T? {
        val manager = managerRef.get()
        return checkNotNull(manager).findFragmentByTag(tag) as T
    }

    companion object {
        lateinit var instance: FragmentNavigation

        @Synchronized
        fun initialize(manager: FragmentManager?) {
            instance = FragmentNavigation(manager)
        }
    }

    init {
        managerRef = WeakReference(checkNotNull(manager))
    }
}