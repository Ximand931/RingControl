package com.happs.ximand.ringcontrol.view.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentExceptionBinding;
import com.happs.ximand.ringcontrol.model.object.exception.bl.BluetoothException;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.viewmodel.fragment.ExceptionViewModel;

public class ExceptionFragment extends BaseFragment<ExceptionViewModel, FragmentExceptionBinding> {

    private BluetoothException exception;

    public ExceptionFragment() {
        super(R.layout.fragment_exception, MENU_NONE);
    }

    public static ExceptionFragment newInstance(BluetoothException e) {
        ExceptionFragment exceptionFragment = new ExceptionFragment();
        exceptionFragment.exception = e;
        return exceptionFragment;
    }

    @Override
    protected void onSetActionBarTitle(@NonNull ActionBar actionBar) {
        actionBar.setTitle(R.string.title_exception);
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull ExceptionViewModel viewModel) {
        if (exception != null) {
            viewModel.setException(exception);
        }
        viewModel.getRestartApplicationLiveEvent().observe(
                getViewLifecycleOwner(), this::onRestartAppEvent
        );
    }

    private void onRestartAppEvent(Void aVoid) {
        Context context = getContext();
        if (context != null) {
            restartApplication(context);
        }
    }

    private void restartApplication(Context context) {
        Intent intent = getLaunchIntent(context);
        if (intent != null) {
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
        }
        Runtime.getRuntime().exit(0);
    }

    private Intent getLaunchIntent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getLaunchIntentForPackage(context.getPackageName());
    }
}
