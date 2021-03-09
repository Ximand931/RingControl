package com.happs.ximand.ringcontrol.view.fragment

import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentAllTimetablesBinding
import com.happs.ximand.ringcontrol.databinding.ItemTimetableBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.view.adapter.AllTimetablesRecyclerViewAdapter
import com.happs.ximand.ringcontrol.viewmodel.fragment.AllTimetablesViewModel

class AllTimetablesFragment : BaseFragmentWithRecyclerView<AllTimetablesViewModel, FragmentAllTimetablesBinding, Timetable, ItemTimetableBinding, AllTimetablesRecyclerViewAdapter>(R.layout.fragment_all_timetables, R.menu.menu_toolbar_main) {

    companion object {

        @JvmField
        val TAG = AllTimetablesFragment::class.java.simpleName

        const val EVENT_APPLIED_TIMETABLE_UPDATED = 1
        const val EVENT_TIMETABLE_LIST_UPDATED = 2

        @JvmStatic
        fun newInstance(): AllTimetablesFragment {
            return AllTimetablesFragment()
        }
    }

    override fun getRecyclerViewFromBinding(binding: FragmentAllTimetablesBinding): RecyclerView? {
        return binding.allTimetablesRecyclerView
    }

    override fun onPreViewModelAttaching(viewModel: AllTimetablesViewModel) {
        viewModel.allTimetablesLiveData.observe(
                viewLifecycleOwner, Observer { initAdapter(it) })
    }

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        actionBar.setTitle(R.string.all_timetables)
    }

    override fun onPreAttachRecyclerViewAdapter(adapter: AllTimetablesRecyclerViewAdapter) {
        adapter.applyingPossible = requireViewModel().applyingPossible
        adapter.appliedTimetableId = requireViewModel().lastAppliedTimetableId
        adapter.timetableClickHandler = AllTimetablesRecyclerViewAdapter.TimetableClickHandler { requireViewModel().applyTimetable(it) }
        //adapter.detailsTimetableClickListener = OnEventListener { requireViewModel().showTimetableDetails(it) }
    }

    override fun onExternalEvent(eventId: Int) {
        if (eventId == EVENT_TIMETABLE_LIST_UPDATED) {
            requireViewModel().updateData()
        } else if (eventId == EVENT_APPLIED_TIMETABLE_UPDATED) {
            requireViewModel().notifyAppliedTimetableUpdated()
        }
    }

    override fun createNewAdapter(items: MutableList<Timetable>): AllTimetablesRecyclerViewAdapter {
        return AllTimetablesRecyclerViewAdapter(items)
    }
}