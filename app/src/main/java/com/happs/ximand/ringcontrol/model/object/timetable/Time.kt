package com.happs.ximand.ringcontrol.model.`object`.timetable

class Time {
    var hours = 0
        private set
    var minutes = 0
        private set
    var seconds = 0
        private set
    private var detailedTime: String? = null

    constructor(hours: Int, minutes: Int, seconds: Int) {
        setTime(hours, minutes, seconds)
        updateDetailedTime()
    }

    constructor(detailedTime: String) {
        val hours = detailedTime.substring(0, 2).toInt()
        val minutes = detailedTime.substring(3, 5).toInt()
        val seconds = detailedTime.substring(6, 8).toInt()
        setTime(hours, minutes, seconds)
        updateDetailedTime()
    }

    private fun setTime(hours: Int, minutes: Int, seconds: Int) {
        this.hours = hours
        this.minutes = minutes
        this.seconds = seconds
    }

    override fun toString(): String {
        return detailedTime!!
    }

    private fun updateDetailedTime() {
        val timeBuilder = StringBuilder()
        addOnePartOfTime(hours, timeBuilder)
        timeBuilder.append(":")
        addOnePartOfTime(minutes, timeBuilder)
        timeBuilder.append(":")
        addOnePartOfTime(seconds, timeBuilder)
        detailedTime = timeBuilder.toString()
    }

    private fun addOnePartOfTime(`val`: Int, timeBuilder: StringBuilder) {
        addZeroIfNecessary(`val`, timeBuilder)
        timeBuilder.append(`val`)
    }

    private fun addZeroIfNecessary(`val`: Int, timeBuilder: StringBuilder) {
        if (`val` < 10) {
            timeBuilder.append("0")
        }
    }

    companion object {
        @JvmStatic
        val midnightTime: Time
            get() = Time(0, 0, 0)
    }
}