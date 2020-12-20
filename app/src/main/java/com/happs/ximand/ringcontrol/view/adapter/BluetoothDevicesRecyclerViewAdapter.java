package com.happs.ximand.ringcontrol.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemDeviceBinding;

import java.util.List;

public class BluetoothDevicesRecyclerViewAdapter extends BaseRecyclerViewAdapter<BluetoothDevice,
        BluetoothDevicesRecyclerViewAdapter.DeviceViewHolder> {

    private OnEventListener<BluetoothDevice> deviceSelectedListener;
    private ObservableBoolean connecting;

    public BluetoothDevicesRecyclerViewAdapter(List<BluetoothDevice> items) {
        super(items);
    }

    public void setDeviceSelectedListener(OnEventListener<BluetoothDevice> listener) {
        this.deviceSelectedListener = listener;
    }

    public void setConnectingDeviceAddress(ObservableBoolean connecting) {
        this.connecting = connecting;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDeviceBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_device, parent, false);
        return new DeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        holder.bind(getItems().get(position), deviceSelectedListener, connecting);
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {

        private ItemDeviceBinding binding;

        DeviceViewHolder(@NonNull ItemDeviceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BluetoothDevice device, OnEventListener<BluetoothDevice> selectedListener,
                  ObservableBoolean connecting) {
            binding.setDevice(device);
            binding.setConnectingEvent(connecting);
            binding.getRoot().setOnClickListener(v -> selectedListener.onEvent(device));
            binding.executePendingBindings();
        }
    }

}
