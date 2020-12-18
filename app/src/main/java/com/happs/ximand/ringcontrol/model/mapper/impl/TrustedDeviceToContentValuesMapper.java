package com.happs.ximand.ringcontrol.model.mapper.impl;

import android.content.ContentValues;

import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.object.TrustedDevice;

import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_DATE;
import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_MANUFACTURER;
import static com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper.DB_FIELD_MODEL;

public class TrustedDeviceToContentValuesMapper implements Mapper<TrustedDevice, ContentValues> {

    @Override
    public ContentValues map(TrustedDevice from) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_FIELD_MANUFACTURER, from.getManufacturer());
        contentValues.put(DB_FIELD_MODEL, from.getModel());
        contentValues.put(DB_FIELD_DATE, from.getDate());
        return contentValues;
    }

}
