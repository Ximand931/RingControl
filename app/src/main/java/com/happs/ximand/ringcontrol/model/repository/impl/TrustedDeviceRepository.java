package com.happs.ximand.ringcontrol.model.repository.impl;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.happs.ximand.ringcontrol.BuildConfig;
import com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper;
import com.happs.ximand.ringcontrol.model.mapper.Mapper;
import com.happs.ximand.ringcontrol.model.mapper.impl.CursorToTrustedDeviceMapper;
import com.happs.ximand.ringcontrol.model.mapper.impl.TrustedDeviceToContentValuesMapper;
import com.happs.ximand.ringcontrol.model.object.TrustedDevice;

public class TrustedDeviceRepository extends BaseRepository<TrustedDevice> {

    private static TrustedDeviceRepository instance;

    public TrustedDeviceRepository(Application application) {
        super(application);
    }

    public static void initialize(Application application) {
        instance = new TrustedDeviceRepository(application);
    }

    public static TrustedDeviceRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TrustedDevicesRepository was not initialized");
        }
        return instance;
    }

    @Override
    protected SQLiteOpenHelper getSQLiteHelper(Application application) {
        return new TrustedDevicesDatabaseHelper(application, BuildConfig.VERSION_CODE);
    }

    @Override
    protected Mapper<Cursor, TrustedDevice> getFromCursorMapper() {
        return new CursorToTrustedDeviceMapper();
    }

    @Override
    protected Mapper<TrustedDevice, ContentValues> getToContentValuesMapper() {
        return new TrustedDeviceToContentValuesMapper();
    }

    @Override
    protected int getItemId(TrustedDevice item) {
        return item.getId();
    }

    @Override
    protected void setItemId(TrustedDevice item, int id) {
        item.setId(id);
    }
}
