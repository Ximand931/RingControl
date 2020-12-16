package com.happs.ximand.ringcontrol.view;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment;
import com.happs.ximand.ringcontrol.viewmodel.ConnectStatus;
import com.happs.ximand.ringcontrol.viewmodel.SharedViewModel;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PROGRESS_BAR_SIZE = 96;

    private SharedViewModel sharedViewModel;
    private Snackbar connectStatusSnackbar;

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
        this.sharedViewModel = createViewModel();
        this.connectStatusSnackbar = makeLoadingSnackbar();
        binding.setSharedViewModel(sharedViewModel);

        requestLocationPermission();

        if (checkLocationPermission()) {
            navigateToAllTimetablesFragment();
        }

        Toolbar toolbar = binding.mainToolbar;
        setSupportActionBar(toolbar);

        observeViewModelEvents();
    }

    private SharedViewModel createViewModel() {
        ViewModelProvider provider =
                new ViewModelProvider(
                        getViewModelStore(),
                        new ViewModelProvider.NewInstanceFactory()
                );
        return provider.get(SharedViewModel.class);
    }

    private void requestLocationPermission() {
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{(Manifest.permission.ACCESS_FINE_LOCATION)},
                    1);
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void navigateToAllTimetablesFragment() {
        FragmentNavigation.getInstance()
                .navigateToFragment(AllTimetablesFragment.newInstance());
        connectStatusSnackbar.show();
    }

    private void observeViewModelEvents() {
        sharedViewModel.getRegisterReceiverEvent().observe(
                this, this::registerReceiver
        );
        sharedViewModel.getEnableBluetoothRequestEvent().observe(
                this, this::suggestUserToEnableBluetooth
        );
        sharedViewModel.getConnectStatusLiveData().observe(
                this, this::changeSnackbarContent
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToAllTimetablesFragment();
            } else {
                sharedViewModel.notifyUserRefuseToProvideLocation();
            }
        }
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
                sharedViewModel.notifyUserEnabledBluetooth();
            } else {
                sharedViewModel.notifyUserRefusedToEnableBluetooth();
            }
        }
    }

    private void registerReceiver(BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);
    }

    private void changeSnackbarContent(ConnectStatus connectStatus) {
        switch (connectStatus) {
            case WAITING_FOR_BLUETOOTH:
                connectStatusSnackbar.setText(R.string.waiting_for_bluetooth);
                break;
            case SEARCHING:
                connectStatusSnackbar.setText(R.string.searching_device);
                break;
            case CONNECTING:
                connectStatusSnackbar.setText(R.string.connecting_to_device);
                break;
            case ERROR:
                connectStatusSnackbar.dismiss();
            case CONNECTED:
                connectStatusSnackbar.dismiss();
                makeConnectedSnackbar().show();
                break;
        }
    }

    private Snackbar makeConnectedSnackbar() {
        View container = findViewById(R.id.container_main);
        Snackbar connectedSnackbar = Snackbar
                .make(container, R.string.connected, Snackbar.LENGTH_SHORT);
        addSuccessfullyConnectedIconToSnackbar(connectedSnackbar);
        return connectedSnackbar;
    }

    private void addSuccessfullyConnectedIconToSnackbar(Snackbar snackbar) {
        View snackbarLayout = snackbar.getView();
        TextView textView = (TextView) snackbarLayout
                .findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_check, 0, 0, 0);
        textView.setCompoundDrawablePadding(
                getResources().getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
    }

    private Snackbar makeLoadingSnackbar() {
        View container = findViewById(R.id.container_main);
        Snackbar snackbar = Snackbar
                .make(container, R.string.initialization, Snackbar.LENGTH_INDEFINITE);
        addProgressBarToSnackbar(snackbar);
        return snackbar;
    }

    private void addProgressBarToSnackbar(Snackbar snackbar) {
        ViewGroup snackbarContent = (ViewGroup) snackbar.getView()
                .findViewById(R.id.snackbar_text).getParent();
        snackbarContent.addView(createProgressBarForSnackbar(), 0);
    }

    private ProgressBar createProgressBarForSnackbar() {
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(createLayoutParamsForProgressBar());
        progressBar.setPadding(0, 16, 0, 16);
        progressBar.setIndeterminateTintList(getColorStateListForProgressBar());
        return progressBar;
    }

    private LinearLayout.LayoutParams createLayoutParamsForProgressBar() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                PROGRESS_BAR_SIZE, PROGRESS_BAR_SIZE
        );
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    private ColorStateList getColorStateListForProgressBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColorStateList(R.color.colorProgressBar);
        } else {
            int progressBarColor = ContextCompat
                    .getColor(this, R.color.colorProgressBar);
            return ColorStateList.valueOf(progressBarColor);
        }
    }
}
