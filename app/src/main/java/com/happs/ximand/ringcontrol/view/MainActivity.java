package com.happs.ximand.ringcontrol.view;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.FragmentNavigation;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ActivityMainBinding;
import com.happs.ximand.ringcontrol.viewmodel.ActivityViewModel;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 1;

    private ActivityViewModel viewModel;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finishAndRemoveTask();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentNavigation.initialize(getSupportFragmentManager());
        ActivityMainBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_main);
        this.viewModel = createViewModel();
        binding.setSharedViewModel(viewModel);
        checkPermission();

        Toolbar toolbar = binding.mainToolbar;
        setSupportActionBar(toolbar);
        observeViewModelEvents();
        viewModel.afterOnCreate();
    }

    private ActivityViewModel createViewModel() {
        ViewModelProvider provider =
                new ViewModelProvider(
                        getViewModelStore(),
                        new ViewModelProvider.NewInstanceFactory()
                );
        return provider.get(ActivityViewModel.class);
    }

    private void checkPermission() {
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)},
                    REQUEST_LOCATION);
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void observeViewModelEvents() {
        viewModel.getErrorLiveData().observe(
                this, this::showErrorSnackbar
        );
        viewModel.getInfoLiveData().observe(
                this, this::showInfoSnackbar
        );
        viewModel.getEnableBluetoothLiveEvent().observe(
                this, this::suggestUserToEnableBluetooth
        );
    }

    private void suggestUserToEnableBluetooth(Void aVoid) {
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.onBluetoothEnabled();
            } else {
                viewModel.onRefuseEnableBluetooth();
            }
        }
    }

    private void showErrorSnackbar(int messageResId) {
        showSnackbarWithIcon(messageResId, R.drawable.ic_snackbar_error);
    }

    private void showInfoSnackbar(int messageResId) {
        showSnackbarWithIcon(messageResId, R.drawable.ic_snackbar_info);
    }

    private void showSnackbarWithIcon(int messageResId, int iconResId) {
        View container = findViewById(R.id.container_main);
        Snackbar connectedSnackbar = Snackbar
                .make(container, messageResId, Snackbar.LENGTH_SHORT);
        addIconToSnackbar(connectedSnackbar, iconResId);
        connectedSnackbar.show();
    }

    private void addIconToSnackbar(Snackbar snackbar, int iconResId) {
        View snackbarLayout = snackbar.getView();
        TextView textView = (TextView) snackbarLayout
                .findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        textView.setCompoundDrawablePadding(
                getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
    }
}
