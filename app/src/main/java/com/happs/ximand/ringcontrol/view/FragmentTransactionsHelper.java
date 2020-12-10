package com.happs.ximand.ringcontrol.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.happs.ximand.ringcontrol.R;

public class FragmentTransactionsHelper {

    private static final int CONTAINER_ID = R.id.container_main;

    @SuppressWarnings("rawtypes")
    public static void replaceFragment(FragmentManager manager, BaseFragment fragment) {

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fade_in, R.animator.fade_out,
                R.animator.fade_in, R.animator.fade_out
        );
        transaction.replace(CONTAINER_ID, fragment, fragment.getDefaultTag());
        transaction.addToBackStack(fragment.getDefaultTag());
        transaction.commitAllowingStateLoss();
    }

}
