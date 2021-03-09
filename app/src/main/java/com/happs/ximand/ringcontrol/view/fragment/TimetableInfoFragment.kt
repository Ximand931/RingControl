package com.happs.ximand.ringcontrol.view.fragment

import android.content.DialogInterface
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentTimetableInfoBinding
import com.happs.ximand.ringcontrol.databinding.ItemTimeBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.view.adapter.TimeRecyclerViewAdapter
import com.happs.ximand.ringcontrol.viewmodel.fragment.TimetableInfoViewModel

class TimetableInfoFragment : BaseFragmentWithRecyclerView<TimetableInfoViewModel,
        FragmentTimetableInfoBinding, Lesson, ItemTimeBinding, TimeRecyclerViewAdapter>
(R.layout.fragment_timetable_info, R.menu.menu_toolbar_info) {

    private var timetable: Timetable? = null

    companion object {
        @JvmStatic
        fun newInstance(timetable: Timetable?): TimetableInfoFragment {
            val fragment = TimetableInfoFragment()
            fragment.timetable = timetable
            return fragment
        }
    }

    override fun getRecyclerViewFromBinding(binding: FragmentTimetableInfoBinding): RecyclerView {
        return binding.timetableInfoRecyclerView
    }

    override fun onPreViewModelAttaching(viewModel: TimetableInfoViewModel) {
        if (timetable != null) {
            viewModel.setTimetable(timetable!!)
        }
        viewModel.timetableLiveData.observe(viewLifecycleOwner,
                Observer<Timetable> { timetable: Timetable ->
                    initAdapter(timetable.lessons as MutableList<Lesson>)
                }
        )
        viewModel.alertDialogLiveEvent.observe(
                viewLifecycleOwner, Observer { onPositiveClick: DialogInterface.OnClickListener ->
            onCreateAlertDialog(onPositiveClick)
        })
    }

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        if (timetable != null) {
            actionBar.title = timetable!!.title
        }
    }

    override fun createNewAdapter(items: MutableList<Lesson>): TimeRecyclerViewAdapter {
        return TimeRecyclerViewAdapter(items)
    }

    private fun onCreateAlertDialog(onPositiveClick: DialogInterface.OnClickListener) {
        if (context == null) {
            return
        }
        MaterialAlertDialogBuilder(context!!)
                .setTitle(R.string.removing)
                .setMessage(R.string.removing_description)
                .setPositiveButton(R.string.remove, onPositiveClick)
                .setNegativeButton(
                        R.string.cancel
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                .create()
                .show()
    }

}