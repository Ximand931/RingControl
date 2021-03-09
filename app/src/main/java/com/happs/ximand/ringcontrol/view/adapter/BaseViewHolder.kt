package com.happs.ximand.ringcontrol.view.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T, B : ViewDataBinding>(protected var binding: B) :
        RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(item: T, position: Int = -1)

}