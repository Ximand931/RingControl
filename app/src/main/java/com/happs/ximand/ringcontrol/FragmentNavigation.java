package com.happs.ximand.ringcontrol;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.FragmentTransactionsHelper;

import java.lang.ref.WeakReference;

public class FragmentNavigation {

    private static FragmentNavigation instance;

    private WeakReference<FragmentManager> managerRef;

    private FragmentNavigation(FragmentManager manager) {
        if (manager == null) {
            throw new NullPointerException("Fragment manager is null");
        }
        this.managerRef = new WeakReference<>(manager);
    }

    public static FragmentNavigation getInstance() {
        if (instance == null) {
            throw new NullPointerException("Instance of fragment navigation " +
                    "was not initialized");
        }
        return instance;
    }

    public synchronized static void initialize(FragmentManager manager) {
        instance = new FragmentNavigation(manager);
    }

    public void navigateToPreviousFragment() {
        FragmentManager manager = managerRef.get();
        if (manager != null) {
            manager.popBackStackImmediate();
        }
    }

    public void navigateTo(Fragment fragment) {
        FragmentManager manager = managerRef.get();
        if (manager != null) {
            FragmentTransactionsHelper.replaceFragment(manager, fragment);
        } else {
            throw new NullPointerException("FragmentManager is null");
        }
    }

    @SuppressWarnings("rawtypes")
    public void notifyFragmentAboutEvent(String tag, int eventId) {
        BaseFragment fragment = findFragmentByTag(tag);
        if (fragment != null) {
            fragment.onExternalEvent(eventId);
        } else {
            throw new NullPointerException("Fragment was not found by tag or fragment manager " +
                    "has null reference");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <T extends BaseFragment> T findFragmentByTag(String tag) {
        FragmentManager manager = managerRef.get();
        if (manager != null) {
            return (T) manager.findFragmentByTag(tag);
        }
        return null;
    }
}
