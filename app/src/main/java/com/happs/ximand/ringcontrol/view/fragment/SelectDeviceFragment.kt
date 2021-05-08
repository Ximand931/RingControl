package com.happs.ximand.ringcontrol.view.fragment

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentSelectDevicesBinding
import com.happs.ximand.ringcontrol.databinding.ItemDeviceBinding
import com.happs.ximand.ringcontrol.view.adapter.BluetoothDevicesRecyclerViewAdapter
import com.happs.ximand.ringcontrol.viewmodel.fragment.SelectDeviceViewModel

class SelectDeviceFragment : BaseFragmentWithRecyclerView<SelectDeviceViewModel,
        FragmentSelectDevicesBinding, BluetoothDevice, ItemDeviceBinding, BluetoothDevicesRecyclerViewAdapter>
(R.layout.fragment_select_devices, MENU_NONE) {

    companion object {
        @JvmStatic
        fun newInstance(): SelectDeviceFragment {
            return SelectDeviceFragment()
        }
    }

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        actionBar.setTitle(R.string.connect)
    }

    override fun onViewDataBindingCreated(binding: FragmentSelectDevicesBinding) {
        super.onViewDataBindingCreated(binding)
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            onRefresh()
        }
    }

    private fun onRefresh() {
        requireViewModel().updateData(requireContext()) {
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    override fun getRecyclerViewFromBinding(binding: FragmentSelectDevicesBinding): RecyclerView {
        return binding.devicesRecyclerView
    }

    override fun onPreAttachRecyclerViewAdapter(adapter: BluetoothDevicesRecyclerViewAdapter) {
        adapter.deviceSelectedListener = requireViewModel()::connectToDevice
        adapter.connecting = requireViewModel().connecting
    }

    override fun onPreViewModelAttaching(viewModel: SelectDeviceViewModel) {
        viewModel.devicesLiveData.observe(viewLifecycleOwner, Observer {
            initAdapter(it)
        })
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    override fun createNewAdapter(items: MutableList<BluetoothDevice>): BluetoothDevicesRecyclerViewAdapter {
        return BluetoothDevicesRecyclerViewAdapter(items)
    }
}