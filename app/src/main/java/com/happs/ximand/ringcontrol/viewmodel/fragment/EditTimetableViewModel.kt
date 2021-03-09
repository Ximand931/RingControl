package com.happs.ximand.ringcontrol.viewmodel.fragment

import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.dao.SharedPreferencesDao
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment

class EditTimetableViewModel : BaseEditTimetableViewModel() {

    private var editingTimetable: Timetable? = null

    fun setEditingTimetable(editingTimetable: Timetable) {
        this.editingTimetable = editingTimetable
        setLessonList(editingTimetable.lessons as MutableList<Lesson>)
        titleLiveData.value = editingTimetable.title
    }

    override fun onCompleteEditAction() {
        updateEditingTimetableWithInputData()
        TimetableRepository.getInstance().update(editingTimetable!!)
    }

    override fun onEditActionCompleted() {
        super.onEditActionCompleted()
        if (isAppliedTimetable()) {
            notifyMainFragmentAboutAppliedTimetableUpdated()
        }
    }

    override fun getErrorMessageResId(): Int {
        return R.string.timetable_was_not_updated
    }

    private fun updateEditingTimetableWithInputData() {
        editingTimetable?.title = titleLiveData.value.toString()
        editingTimetable?.lessons = lessonsLiveData.value!!
    }

    private fun isAppliedTimetable(): Boolean {
        return editingTimetable?.id ==
                SharedPreferencesDao.getInstance().getAppliedTimetableId()
    }

    private fun notifyMainFragmentAboutAppliedTimetableUpdated() {
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(
                AllTimetablesFragment.TAG, AllTimetablesFragment.EVENT_APPLIED_TIMETABLE_UPDATED
        )
    }
}