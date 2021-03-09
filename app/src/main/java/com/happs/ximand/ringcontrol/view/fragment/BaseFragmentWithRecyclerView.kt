package com.happs.ximand.ringcontrol.view.fragment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happs.ximand.ringcontrol.view.adapter.BaseRecyclerViewAdapter
import com.happs.ximand.ringcontrol.view.adapter.BaseViewHolder
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseViewModel

abstract class BaseFragmentWithRecyclerView<VM : BaseViewModel, B : ViewDataBinding, T, RB : ViewDataBinding,
        A : BaseRecyclerViewAdapter<T, out BaseViewHolder<T, RB>, RB>>(layoutId: Int, menuResId: Int)
    : BaseFragment<VM, B>(layoutId, menuResId) {

    private var recyclerView: RecyclerView? = null

    @Suppress("UNCHECKED_CAST")
    protected val adapter: A?
        get() = recyclerView!!.adapter as A?

    override fun onViewDataBindingCreated(binding: B) {
        recyclerView = getRecyclerViewFromBinding(binding)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
    }

    protected abstract fun getRecyclerViewFromBinding(binding: B): RecyclerView?

    protected fun initAdapter(items: MutableList<T>?) {
        checkNotNull(items)
        val adapter = adapter
        val newAdapter = createNewAdapter(items)
        onPreAttachRecyclerViewAdapter(newAdapter)
        recyclerView!!.adapter = newAdapter
        if (adapter != null) {
            recyclerView!!.invalidate()
        }
    }

    protected abstract fun createNewAdapter(items: MutableList<T>): A

    protected open fun onPreAttachRecyclerViewAdapter(adapter: A) {}
}