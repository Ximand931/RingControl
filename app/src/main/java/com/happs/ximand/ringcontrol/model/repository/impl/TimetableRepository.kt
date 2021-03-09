package com.happs.ximand.ringcontrol.model.repository.impl

import android.app.Application
import com.happs.ximand.ringcontrol.BuildConfig
import com.happs.ximand.ringcontrol.model.`object`.timetable.Timetable
import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.impl.CursorToTimetableMapper
import com.happs.ximand.ringcontrol.model.mapper.impl.TimetableToContentValuesMapper

class TimetableRepository(application: Application) : BaseRepository<Timetable>() {

    override val sqLiteHelper = TimetableDatabaseHelper(application, BuildConfig.VERSION_CODE)
    override val fromCursorMapper = CursorToTimetableMapper()
    override val toContentValuesMapper = TimetableToContentValuesMapper()

    companion object {

        private var instance: TimetableRepository? = null

        fun initialize(application: Application) {
            instance = TimetableRepository(application)
        }

        fun getInstance(): TimetableRepository {
            return checkNotNull(instance) { "TimetableRepository was not initialized" }
        }
    }

    override fun getItemId(item: Timetable): Int {
        return item.id
    }

    override fun setItemId(item: Timetable, id: Int) {
        item.id = id
    }

}