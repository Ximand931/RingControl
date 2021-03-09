package com.happs.ximand.ringcontrol.model.`object`.command.simple

enum class WeekendMode(val modeId: Byte) {

    MODE_NOT_WORK_ON_WEEKENDS(0.toByte()),
    MODE_WORK_ON_SATURDAY(1.toByte()),
    MODE_WORK_ON_WEEKENDS(2.toByte());

    companion object {
        fun getInstanceForModeId(modeId: Int): WeekendMode {
            for (weekendMode in values()) {
                if (weekendMode.modeId.toInt() == modeId) {
                    return weekendMode
                }
            }
            throw IllegalArgumentException()
        }
    }

}