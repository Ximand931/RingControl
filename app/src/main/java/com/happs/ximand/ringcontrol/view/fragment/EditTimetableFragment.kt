package com.happs.ximand.ringcontrol.view.fragment

import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentEditTimetableBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.viewmodel.fragment.EditTimetableViewModel

class EditTimetableFragment : BaseEditTimetableFragment<EditTimetableViewModel, FragmentEditTimetableBinding>(R.layout.fragment_edit_timetable) {

    private var timetable: Timetable? = null

    companion object {

        @JvmStatic
        fun newInstance(timetable: Timetable?): EditTimetableFragment {
            val fragment = EditTimetableFragment()
            fragment.timetable = timetable
            return fragment
        }
    }

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        actionBar.setTitle(R.string.edit)
    }

    override fun onPreViewModelAttaching(viewModel: EditTimetableViewModel) {
        if (timetable != null) {
            viewModel.setEditingTimetable(timetable!!)
        }
        super.onPreViewModelAttaching(viewModel)
    }

    override fun getRecyclerViewFromBinding(binding: FragmentEditTimetableBinding): RecyclerView? {
        return binding.lessonsRecyclerView
    }
}
