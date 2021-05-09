package com.happs.ximand.ringcontrol.model.`object`.command.simple

import com.happs.ximand.ringcontrol.R

enum class WeekendMode(val modeId: Byte, val descriptionResId: Int) {

    MODE_NOT_WORK_ON_WEEKENDS(0.toByte(), R.string.mode_not_work_on_weekends),
    MODE_WORK_ON_SATURDAY(1.toByte(), R.string.mode_work_on_saturday),
    MODE_WORK_ON_WEEKENDS(2.toByte(), R.string.mode_work_on_weekends);

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