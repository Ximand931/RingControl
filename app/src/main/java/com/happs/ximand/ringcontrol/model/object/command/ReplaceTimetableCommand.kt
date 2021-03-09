package com.happs.ximand.ringcontrol.model.`object`.command

import com.happs.ximand.ringcontrol.model.`object`.timetable.Lesson
import com.happs.ximand.ringcontrol.model.`object`.timetable.Time
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable

class ReplaceTimetableCommand(newTimetable: Timetable) : BluetoothCommand<Void>(REPLACE_TIMETABLE_COMMAND_CODE) {

    private val command: ByteArray
    private val timetable: Timetable

    override fun toByteArray(): ByteArray {
        appendMeta()
        for (i in timetable.lessons.indices) {
            val lesson: Lesson = timetable.lessons[i]
            appendTime(i * 6 + 3, lesson.startTime)
            appendTime(i * 6 + 6, lesson.endTime)
        }
        appendEndOfLine()
        return command
    }

    override fun getMainContent(): Void {
        throw UnsupportedOperationException()
    }

    private fun appendMeta() {
        command[0] = code
        command[1] = TIMETABLE_ID_STUB
        command[2] = (timetable.lessons.size * 2).toByte()
    }

    private fun appendTime(startPos: Int, time: Time) {
        command[startPos] = time.hours.toByte()
        command[startPos + 1] = time.minutes.toByte()
        command[startPos + 2] = time.seconds.toByte()
    }

    private fun appendEndOfLine() {
        command[command.size - 1] = 1
    }

    companion object {
        private const val REPLACE_TIMETABLE_COMMAND_CODE: Byte = 11
        private const val TIMETABLE_ID_STUB: Byte = 1
    }

    init {
        val timeSize: Int = newTimetable.lessons.size * 2 * 3
        command = ByteArray(3 + timeSize + 1)
        timetable = newTimetable
    }
}