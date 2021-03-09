package com.happs.ximand.ringcontrol.view.adapter

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ItemTimeBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.view.adapter.TimeRecyclerViewAdapter.TimeViewHolder

class TimeRecyclerViewAdapter(lessons: MutableList<Lesson>) : BaseRecyclerViewAdapter<Lesson,
        TimeViewHolder, ItemTimeBinding>(lessons, R.layout.item_time) {

    override fun createViewHolderByBinding(binding: ItemTimeBinding): TimeViewHolder {
        return TimeViewHolder(binding)
    }

    class TimeViewHolder(binding: ItemTimeBinding) : BaseViewHolder<Lesson, ItemTimeBinding>(binding) {
        override fun onBind(item: Lesson, position: Int) {
            binding.lesson = item
            binding.executePendingBindings()
        }
    }
}