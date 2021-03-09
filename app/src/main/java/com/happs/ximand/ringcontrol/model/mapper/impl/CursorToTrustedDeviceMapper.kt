package com.happs.ximand.ringcontrol.model.mapper.impl

import android.database.Cursor
import com.happs.ximand.ringcontrol.model.`object`.TrustedDevice
import com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.Mapper

class CursorToTrustedDeviceMapper : Mapper<Cursor, TrustedDevice> {
    override fun map(from: Cursor): TrustedDevice {
        val idColumnIndex = from.getColumnIndex(TrustedDevicesDatabaseHelper.DB_FIELD_ID)
        val manufacturerColumnIndex = from.getColumnIndex(TrustedDevicesDatabaseHelper.DB_FIELD_MANUFACTURER)
        val modelColumnIndex = from.getColumnIndex(TrustedDevicesDatabaseHelper.DB_FIELD_MODEL)
        val dateColumnIndex = from.getColumnIndex(TrustedDevicesDatabaseHelper.DB_FIELD_DATE)
        val id = from.getInt(idColumnIndex)
        val manufacturer = from.getString(manufacturerColumnIndex)
        val model = from.getString(modelColumnIndex)
        val date = from.getString(dateColumnIndex)
        return TrustedDevice(id, manufacturer, model, date)
    }
}