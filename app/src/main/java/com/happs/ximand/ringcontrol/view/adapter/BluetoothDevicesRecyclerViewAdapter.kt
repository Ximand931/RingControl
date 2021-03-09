package com.happs.ximand.ringcontrol.view.adapter

import android.bluetooth.BluetoothDevice
import androidx.databinding.ObservableBoolean
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ItemDeviceBinding
import com.happs.ximand.ringcontrol.view.adapter.BluetoothDevicesRecyclerViewAdapter.DeviceViewHolder

class BluetoothDevicesRecyclerViewAdapter(items: MutableList<BluetoothDevice>) : BaseRecyclerViewAdapter
<BluetoothDevice, DeviceViewHolder, ItemDeviceBinding>(items, R.layout.item_device) {

    var deviceSelectedListener: ((dev: BluetoothDevice) -> Unit)? = null
    var connecting: ObservableBoolean? = null

    override fun createViewHolderByBinding(binding: ItemDeviceBinding): DeviceViewHolder {
        return DeviceViewHolder(binding)
    }

    inner class DeviceViewHolder(binding: ItemDeviceBinding) : BaseViewHolder<BluetoothDevice, ItemDeviceBinding>(binding) {

        override fun onBind(item: BluetoothDevice, position: Int) {
            binding.device = item
            binding.connectingEvent = connecting
            binding.root.setOnClickListener {
                deviceSelectedListener!!.invoke(item)
            }
            binding.executePendingBindings()
        }

    }

}