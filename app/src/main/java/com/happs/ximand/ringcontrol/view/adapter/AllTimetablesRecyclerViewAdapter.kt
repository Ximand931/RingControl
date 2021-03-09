package com.happs.ximand.ringcontrol.view.adapter

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ItemTimetableBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.view.adapter.AllTimetablesRecyclerViewAdapter.AllTimetablesViewHolder

class AllTimetablesRecyclerViewAdapter(timetables: MutableList<Timetable>) : BaseRecyclerViewAdapter<Timetable, AllTimetablesViewHolder, ItemTimetableBinding>(timetables, R.layout.item_timetable) {

    var applyingPossible: ObservableBoolean? = null
    var appliedTimetableId: ObservableInt? = null
    var timetableClickHandler = TimetableClickHandler()

    override fun createViewHolderByBinding(binding: ItemTimetableBinding): AllTimetablesViewHolder {
        return AllTimetablesViewHolder(binding)
    }

    inner class AllTimetablesViewHolder(binding: ItemTimetableBinding) : BaseViewHolder<Timetable, ItemTimetableBinding>(binding) {
        override fun onBind(item: Timetable, position: Int) {
            binding.timetable = item
            binding.applyingPossible = applyingPossible
            binding.appliedTimetableId = appliedTimetableId
            binding.timetableClickHandler = timetableClickHandler
            binding.executePendingBindings()
        }
    }

    class TimetableClickHandler(
            var applyTimetableClickListener: ((timetable: Timetable) -> Unit)? = null,
            var detailsTimetableClickListener: ((timetable: Timetable) -> Unit)? = null
    )
}