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
        if (manager != null) {
            replaceFragment(manager, fragment!!)
        } else {
            throw NullPointerException("FragmentManager is null")
        }
    }

    fun notifyFragmentAboutEvent(tag: String, eventId: Int) {
        val fragment = findFragmentByTag<BaseFragment<*, *>>(tag)
        if (fragment != null) {
            fragment.onExternalEvent(eventId)
        } else {
            throw NullPointerException("Fragment was not found by tag or fragment manager " +
                    "has null reference")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : BaseFragment<*, *>?> findFragmentByTag(tag: String): T? {
        val manager = managerRef.get()
        return if (manager != null) {
            manager.findFragmentByTag(tag) as T
        } else null
    }

    companion object {
        private var instance: FragmentNavigation? = null

        fun getInstance(): FragmentNavigation {
            return checkNotNull(instance) {
                "Instance of fragment navigation " +
                        "was not initialized"
            }
        }

        @Synchronized
        fun initialize(manager: FragmentManager?) {
            instance = FragmentNavigation(manager)
        }
    }

    init {
        if (manager == null) {
            throw NullPointerException("Fragment manager is null")
        }
        managerRef = WeakReference(manager)
    }
}