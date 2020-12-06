package com.happs.ximand.ringcontrol.view.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentExceptionBinding;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.viewmodel.fragment.ExceptionViewModel;

public class ExceptionFragment extends BaseFragment<ExceptionViewModel, FragmentExceptionBinding> {

    private static final String FRAGMENT_TAG = "Exception";
    private static final String KEY_EXCEPTION = "EXCEPTION";

    private BluetoothException exception;

    public ExceptionFragment() {
        super(R.layout.fragment_exception, FRAGMENT_TAG);
    }

    public static ExceptionFragment newInstance(BluetoothException e) {
        ExceptionFragment exceptionFragment = new ExceptionFragment();
        exceptionFragment.exception = e;
        return exceptionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (exception == null && savedInstanceState != null) {
            this.exception = getExceptionFromArgs(savedInstanceState);
        }
    }

    private BluetoothException getExceptionFromArgs(Bundle args) {
        if (args.containsKey(KEY_EXCEPTION)) {
            return (BluetoothException) args.getSerializable(KEY_EXCEPTION);
        }
        throw new RuntimeException(); //TODO
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull ExceptionViewModel viewModel) {
        viewModel.setException(exception);
        viewModel.getRestartApplicationLiveEvent().observe(
                getViewLifecycleOwner(), this::onRestartAppEvent
        );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_EXCEPTION, exception);
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
