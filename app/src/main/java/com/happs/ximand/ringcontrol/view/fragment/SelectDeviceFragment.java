package com.happs.ximand.ringcontrol.view.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentSelectDevicesBinding;
import com.happs.ximand.ringcontrol.view.adapter.BluetoothDevicesRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.SelectDeviceViewModel;

import java.util.List;

public class SelectDeviceFragment extends BaseFragmentWithRecyclerView<SelectDeviceViewModel,
        FragmentSelectDevicesBinding, BluetoothDevice, BluetoothDevicesRecyclerViewAdapter> {

    public SelectDeviceFragment() {
        super(R.layout.fragment_select_devices, 0);
    }

    public static SelectDeviceFragment newInstance() {
        return new SelectDeviceFragment();
    }

    @Override
    protected RecyclerView getRecyclerViewFromBinding(FragmentSelectDevicesBinding binding) {
        return binding.devicesRecyclerView;
    }

    @Override
    protected void onPreAttachRecyclerViewAdapter(BluetoothDevicesRecyclerViewAdapter adapter) {
        adapter.setDeviceSelectedListener(getViewModel()::notifyDeviceSelected);
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull SelectDeviceViewModel viewModel) {
        viewModel.getDevicesLiveData().observe(getViewLifecycleOwner(), this::initAdapter);
        viewModel.getStartSettingsActivityLiveEvent().observe(
                getViewLifecycleOwner(), this::startSettingsActivity
        );
    }

    private void startSettingsActivity(Void aVoid) {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }

    @Override
    protected BluetoothDevicesRecyclerViewAdapter createNewAdapter(List<BluetoothDevice> items) {
        return new BluetoothDevicesRecyclerViewAdapter(items);
    }
}
