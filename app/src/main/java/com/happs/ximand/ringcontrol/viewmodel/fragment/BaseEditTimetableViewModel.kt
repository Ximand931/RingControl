package com.happs.ximand.ringcontrol.viewmodel.fragment

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.happs.ximand.ringcontrol.FragmentNavigation
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.SingleLiveEvent
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Time
import com.happs.ximand.ringcontrol.view.fragment.AllTimetablesFragment

abstract class BaseEditTimetableViewModel : BaseViewModel() {

    val lessonsLiveData: MutableLiveData<MutableList<Lesson>> = MutableLiveData()
    val numOfLessonsLiveData = MutableLiveData<Int>()
    val titleLiveData = MutableLiveData<String>()
    val titleErrorLiveData = MutableLiveData<Boolean>()
    val detailEditingLiveData = MutableLiveData<Boolean>()
    val addLessonEvent = SingleLiveEvent<Void>()
    val removeLessonEvent = SingleLiveEvent<Void>()
    var correctnessCheck: (() -> Boolean)? = null

    fun addEmptyLesson() {
        addLessonEvent.call()
        numOfLessonsLiveData.value = numOfLessonsLiveData.value!! + 1
    }

    fun removeLastLesson() {
        removeLessonEvent.call()
        numOfLessonsLiveData.value = numOfLessonsLiveData.value!! - 1
    }

    override fun notifyOptionsMenuItemClicked(itemId: Int): Boolean {
        when (itemId) {
            R.id.toolbar_do_edit_action -> {
                completeEditAction()
            }
            R.id.toolbar_cancel -> {
                onCancelClick()
            }
            else -> return false
        }
        return true
    }

    private fun completeEditAction() {
        val correct = checkTitle() && checkLessons()
        if (correct) {
            onCompleteEditAction()
            onEditActionCompleted()
            FragmentNavigation.getInstance().navigateToPreviousFragment()
        } else {
            //makeExceptionSnackbarWithAction(getErrorMessageResId(), ); TODO
        }
    }

    protected abstract fun onCompleteEditAction()
    protected open fun onEditActionCompleted() {
        FragmentNavigation.getInstance().notifyFragmentAboutEvent(
                AllTimetablesFragment.TAG, AllTimetablesFragment.EVENT_TIMETABLE_LIST_UPDATED
        )
    }

    protected abstract fun getErrorMessageResId(): Int

    protected fun setLessonList(lessons: MutableList<Lesson>) {
        lessonsLiveData.value = lessons
        detailEditingLiveData.value = getDetailEditingStatusForCurrentLessonsList(lessons)
    }

    private fun getDetailEditingStatusForCurrentLessonsList(lessons: List<Lesson>): Boolean {
        for (lesson in lessons) {
            val simplifiable = (canTimeBeConvertedToSimple(lesson.startTime)
                    && canTimeBeConvertedToSimple(lesson.endTime))
            if (!simplifiable) {
                return true
            }
        }
        return false
    }

    private fun canTimeBeConvertedToSimple(time: Time): Boolean {
        return time.seconds == 0
    }

    private fun checkTitle(): Boolean {
        val correct = !TextUtils.isEmpty(titleLiveData.value)
        if (!correct) {
            titleErrorLiveData.value = true
        }
        return correct
    }

    private fun checkLessons(): Boolean {
        return correctnessCheck!!.invoke()
    }

    private fun onCancelClick() {
        FragmentNavigation.getInstance().navigateToPreviousFragment()
    }
}