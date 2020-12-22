package com.happs.ximand.ringcontrol.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.happs.ximand.ringcontrol.R;

public class FragmentTransactionsHelper {

    private static final int CONTAINER_ID = R.id.container_main;

    public static void replaceFragment(FragmentManager manager, Fragment fragment) {

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fade_in, R.animator.fade_out,
                R.animator.fade_in, R.animator.fade_out
        );
        String tag = fragment.getClass().getSimpleName();
        transaction.replace(CONTAINER_ID, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

}
