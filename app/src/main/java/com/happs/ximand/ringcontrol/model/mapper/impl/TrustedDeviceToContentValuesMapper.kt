package com.happs.ximand.ringcontrol.model.mapper.impl

import android.content.ContentValues
import com.happs.ximand.ringcontrol.model.`object`.TrustedDevice
import com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.Mapper

class TrustedDeviceToContentValuesMapper : Mapper<TrustedDevice, ContentValues> {
    override fun map(from: TrustedDevice): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(TrustedDevicesDatabaseHelper.DB_FIELD_MANUFACTURER, from.manufacturer)
        contentValues.put(TrustedDevicesDatabaseHelper.DB_FIELD_MODEL, from.model)
        contentValues.put(TrustedDevicesDatabaseHelper.DB_FIELD_DATE, from.date)
        return contentValues
    }
}