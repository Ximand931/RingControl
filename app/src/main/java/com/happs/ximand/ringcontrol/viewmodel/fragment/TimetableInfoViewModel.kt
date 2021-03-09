package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment
import com.happs.ximand.ringcontrol.view.fragment.EditTimetableFragment.Companion.newInstance

class TimetableInfoViewModel : BaseViewModel() {

    //TODO: use DTO
    val alertDialogLiveEvent: SingleLiveEvent<DialogInterface.OnClickListener> = SingleLiveEvent()
    val timetableLiveData: MutableLiveData<Timetable> = MutableLiveData()

    fun setTimetable(timetable: Timetable) {
        timetableLiveData.value = timetable
    }

    override fun notifyOptionsMenuItemClicked(itemId: Int): Boolean {
        return when (itemId) {
            R.id.toolbar_remove -> {
                removeTimetable()
                true
            }
            R.id.toolbar_edit -> {
                FragmentNavigation.getInstance().navigateTo(newInstance(
                        timetableLiveData.value))
                true
            }
            else -> false
        }
    }

    private fun removeTimetable() {
        alertDialogLiveEvent.value = DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
            TimetableRepository.getInstance().remove(timetableLiveData.value!!)
            notifyAllTimetablesFragmentThatTimetableListUpdated()
            FragmentNavigation.getInstance().navigateToPreviousFragment()
        }
    }

    private fun notifyAllTimetablesFragmentThatTimetableListUpdated() {
        val tag = AllTimetablesFragment.TAG
        val id = AllTimetablesFragment.EVENT_TIMETABLE_LIST_UPDATED
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(tag, id)
    }

}