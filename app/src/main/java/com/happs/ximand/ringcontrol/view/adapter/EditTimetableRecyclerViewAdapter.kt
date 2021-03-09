package com.happs.ximand.ringcontrol.view.adapter

import android.text.TextUtils
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding
import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Time
import com.happs.ximand.ringcontrol.view.MaskWatcher
import com.happs.ximand.ringcontrol.view.MaskWatcher.OnLineFilledListener
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter.EditTimetableViewHolder
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtils
import java.util.*

class EditTimetableRecyclerViewAdapter(lessons: MutableList<Lesson>) : BaseRecyclerViewAdapter<Lesson,
        EditTimetableViewHolder, ItemEditLessonBinding>(lessons, R.layout.item_edit_lesson) {

    companion object {
        private const val START_END_TIME_DIVIDER = " – "
    }

    private val inputs: MutableList<ObservableField<String>> = ArrayList()
    private val maskWatchers: MutableList<MaskWatcher> = ArrayList()
    private val errorList: MutableList<ObservableBoolean> = ArrayList()
    private val lineFilledEvent: (linePosition: Int) -> Unit
    private var detailEditing: Boolean? = null

    init {
        lineFilledEvent = { num: Int -> onLineFilled(num) }
    }

    fun setDetailEditingStatus(detailEditing: Boolean) {
        if (this.detailEditing == null) {
            this.detailEditing = detailEditing
            initByLessonList()
            return
        }
        if (this.detailEditing != detailEditing) {
            for (mask in maskWatchers) {
                mask.mask = getMaskForInverseDetailEditingStatus()
            }
            updateInputsForNewDetailEditingStatus()
            this.detailEditing = detailEditing
        }
    }

    private fun initByLessonList() {
        val lessons: List<Lesson> = items
        for (lesson in lessons) {
            inputs.add(ObservableField(
                    getLessonScopes(lesson))
            )
            maskWatchers.add(MaskWatcher(
                    getMaskForCurrentDetailEditingStatus()
            ))
            errorList.add(ObservableBoolean(false))
        }
    }

    fun isAllLinesCorrect(): Boolean {
        for (errorObservable in errorList) {
            if (errorObservable.get()) {
                return false
            }
        }
        return true
    }

    private fun onLineFilled(num: Int) {
        val input = inputs[num].get()
        if (isInputCorrect(input)) {
            updateErrorIdStatusForCorrectLine(num)
            val lesson: Lesson = getLessonForInput(num, input)
            items[num] = lesson
        } else {
            errorList[num].set(true)
        }
    }

    private fun updateErrorIdStatusForCorrectLine(num: Int) {
        val errorObservable = errorList[num]
        if (errorObservable.get()) {
            errorObservable.set(false)
        }
    }

    private fun getLessonForInput(num: Int, input: String?): Lesson {
        val startTime = Time(getDetailedStartTime(input))
        val endTime = Time(getDetailedEndTime(input))
        return Lesson(num + 1, startTime, endTime)
    }

    fun addEmptyLesson() {
        items.add(Lesson(itemCount + 1))
        inputs.add(ObservableField())
        maskWatchers.add(createMaskWatcherForEmptyLesson())
        errorList.add(ObservableBoolean(false))
        notifyItemRangeInserted(items.size - 1, 1)
    }

    private fun createMaskWatcherForEmptyLesson(): MaskWatcher {
        val mask = getMaskForCurrentDetailEditingStatus()
        return MaskWatcher(mask)
    }

    fun removeLastLesson() {
        val lastItemIndex = items.size - 1
        items.removeAt(lastItemIndex)
        inputs.removeAt(lastItemIndex)
        maskWatchers.removeAt(lastItemIndex)
        errorList.removeAt(lastItemIndex)
        notifyItemRemoved(lastItemIndex)
    }

    private fun updateInputsForNewDetailEditingStatus() {
        if (detailEditing!!) {
            updateInputsFromDetailedToSimple()
        } else {
            updateInputsFromSimpleToDetailed()
        }
    }

    private fun updateInputsFromSimpleToDetailed() {
        for (input in inputs) {
            val inputValue = input.get()
            if (isInputCorrect(inputValue)) {
                val startTime = getDetailedStartTime(inputValue)
                val endTime = getDetailedEndTime(inputValue)
                input.set(startTime + START_END_TIME_DIVIDER + endTime)
            }
        }
    }

    private fun updateInputsFromDetailedToSimple() {
        for (input in inputs) {
            val inputValue = input.get()
            if (isInputCorrect(inputValue)) {
                val startTime = getDetailedStartTime(inputValue)
                val endTime = getDetailedEndTime(inputValue)
                input.set(getSimpleLessonScopesFromDetailedTime(startTime, endTime))
            }
        }
    }

    private fun getLessonScopes(lesson: Lesson): String {
        return if (detailEditing!!) {
            (lesson.startTime.toString() + START_END_TIME_DIVIDER
                    + lesson.endTime.toString())
        } else {
            getSimpleLessonScopesFromDetailedTime(lesson.startTime.toString(),
                    lesson.endTime.toString())
        }
    }

    private fun getSimpleLessonScopesFromDetailedTime(startTime: String, endTime: String): String {
        return startTime.substring(0, 5) + START_END_TIME_DIVIDER + endTime.substring(0, 5)
    }

    private fun getDetailedStartTime(input: String?): String {
        return if (detailEditing!!) {
            input!!.substring(0, 8)
        } else {
            input!!.substring(0, 5) + ":00"
        }
    }

    private fun getDetailedEndTime(input: String?): String {
        return if (detailEditing!!) {
            input!!.substring(11)
        } else {
            input!!.substring(8) + ":00"
        }
    }

    private fun isInputCorrect(input: String?): Boolean {
        return (!TextUtils.isEmpty(input)
                && input!!.matches(Regex(getPatternForCurrentDetailedEditingStatus())))
    }

    private fun getPatternForCurrentDetailedEditingStatus(): String {
        return if (detailEditing!!) TimeUtils.DETAILED_TIME_PATTERN else TimeUtils.SIMPLE_TIME_PATTERN
    }

    private fun getMaskForCurrentDetailEditingStatus(): String {
        return if (detailEditing!!) TimeUtils.DETAILED_TIME_MASK else TimeUtils.SIMPLE_TIME_MASK
    }

    private fun getMaskForInverseDetailEditingStatus(): String {
        return if (detailEditing!!) TimeUtils.SIMPLE_TIME_MASK else TimeUtils.DETAILED_TIME_MASK
    }

    override fun createViewHolderByBinding(binding: ItemEditLessonBinding): EditTimetableViewHolder {
        return EditTimetableViewHolder(binding)
    }

    inner class EditTimetableViewHolder(binding: ItemEditLessonBinding) : BaseViewHolder<Lesson, ItemEditLessonBinding>(binding) {

        private val timeEditText: EditText

        private fun attachMaskWatcher(maskWatcher: MaskWatcher,
                                      lineFilledEvent: (linePosition: Int) -> Unit) {
            maskWatcher.setLineFilledListener(
                    object : OnLineFilledListener {
                        override fun onFilled() {
                            lineFilledEvent.invoke(adapterPosition)
                        }
                    }
            )
            binding.timeEditText.addTextChangedListener(maskWatcher)
        }

        override fun onBind(item: Lesson, position: Int) {
            binding.viewPosition = adapterPosition
            binding.input = inputs[position]
            binding.error = errorList[position]
            attachMaskWatcher(maskWatchers[position], lineFilledEvent)
            timeEditText.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                if (!hasFocus) {
                    lineFilledEvent.invoke(adapterPosition)
                }
            }
            binding.executePendingBindings()
        }

        init {
            timeEditText = binding.timeEditText
        }
    }

}