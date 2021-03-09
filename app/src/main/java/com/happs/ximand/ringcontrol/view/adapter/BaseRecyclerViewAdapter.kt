package com.happs.ximand.ringcontrol.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VH : BaseViewHolder<T, B>, B : ViewDataBinding>(
        var items: MutableList<T>,
        private val layoutId: Int
) : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<B>(inflater, layoutId, parent, false)
        return createViewHolderByBinding(binding)
    }

    protected abstract fun createViewHolderByBinding(binding: B): VH

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(items[position])
    }
}