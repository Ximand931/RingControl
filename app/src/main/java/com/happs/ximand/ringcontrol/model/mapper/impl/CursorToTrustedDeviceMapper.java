package com.happs.ximand.ringcontrol.model.mapper.impl;

import android.database.Cursor;

import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.object.TrustedDevice;

import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_DATE;
import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_ID;
import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_MANUFACTURER;
import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_MODEL;

public class CursorToTrustedDeviceMapper implements Mapper<Cursor, TrustedDevice> {

    @Override
    public TrustedDevice map(Cursor from) {
        int idColumnIndex = from.getColumnIndex(DB_FIELD_ID);
        int manufacturerColumnIndex = from.getColumnIndex(DB_FIELD_MANUFACTURER);
        int modelColumnIndex = from.getColumnIndex(DB_FIELD_MODEL);
        int dateColumnIndex = from.getColumnIndex(DB_FIELD_DATE);

        int id = from.getInt(idColumnIndex);
        String manufacturer = from.getString(manufacturerColumnIndex);
        String model = from.getString(modelColumnIndex);
        String date = from.getString(dateColumnIndex);

        return new TrustedDevice(id, manufacturer, model, date);
    }

}
