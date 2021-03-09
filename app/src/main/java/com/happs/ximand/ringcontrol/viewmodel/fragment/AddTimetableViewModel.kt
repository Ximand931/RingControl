package com.happs.ximand.ringcontrol.viewmodel.fragment

import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.repository.impl.TimetableRepository
import java.util.*

class AddTimetableViewModel : BaseEditTimetableViewModel() {

    init {
        setLessonList(ArrayList())
        numOfLessonsLiveData.value = 0
    }

    override fun onCompleteEditAction() {
        val timetable: Timetable = createNewTimetable()
        TimetableRepository.getInstance().add(timetable)
    }

    override fun getErrorMessageResId(): Int {
        return R.string.timetable_was_not_added
    }

    private fun createNewTimetable(): Timetable {
        return Timetable(
                titleLiveData.value!!, lessonsLiveData.value!!
        )
    }
}