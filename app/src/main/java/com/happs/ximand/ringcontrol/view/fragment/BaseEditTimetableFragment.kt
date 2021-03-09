package com.happs.ximand.ringcontrol.view.fragment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseEditTimetableViewModel

abstract class BaseEditTimetableFragment<VM : BaseEditTimetableViewModel, D : ViewDataBinding>(layoutId: Int) : BaseFragmentWithRecyclerView<VM, D, Lesson, ItemEditLessonBinding, EditTimetableRecyclerViewAdapter>(layoutId, R.menu.menu_toolbar_editing) {

    override fun onPreViewModelAttaching(viewModel: VM) {
        viewModel.lessonsLiveData.observe(viewLifecycleOwner,
                Observer { initAdapter(it) })
        viewModel.detailEditingLiveData.observe(viewLifecycleOwner,
                Observer { updateDetailEditingStatus(it) })
        viewModel.correctnessCheck = { isAdapterDataCorrect() }
    }

    override fun createNewAdapter(items: MutableList<Lesson>): EditTimetableRecyclerViewAdapter {
        return EditTimetableRecyclerViewAdapter(items)
    }

    override fun onPreAttachRecyclerViewAdapter(adapter: EditTimetableRecyclerViewAdapter) {
        requireViewModel().addLessonEvent.observe(viewLifecycleOwner,
                Observer { adapter.addEmptyLesson() }
        )
        requireViewModel().removeLessonEvent.observe(viewLifecycleOwner,
                Observer { adapter.removeLastLesson() }
        )
    }

    private fun updateDetailEditingStatus(status: Boolean) {
        val adapter = adapter
        adapter?.setDetailEditingStatus(status)
    }

    private fun isAdapterDataCorrect(): Boolean {
        val adapter = adapter
        return adapter?.isAllLinesCorrect() ?: false
    }
}