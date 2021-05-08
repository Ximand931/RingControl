package com.happs.ximand.ringcontrol.view.fragment

import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentAddTimetableBinding
import com.happs.ximand.ringcontrol.viewmodel.fragment.AddTimetableViewModel

class AddTimetableFragment : BaseEditTimetableFragment<AddTimetableViewModel, FragmentAddTimetableBinding>(R.layout.fragment_add_timetable) {

    companion object {
        @JvmStatic
        fun newInstance(): AddTimetableFragment {
            return AddTimetableFragment()
        }
    }

    override fun getRecyclerViewFromBinding(binding: FragmentAddTimetableBinding): RecyclerView {
        return binding.lessonsRecyclerView
    }

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        actionBar.setTitle(R.string.add_lesson)
    }

}