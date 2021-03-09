package com.happs.ximand.ringcontrol.model.repository.impl

import android.app.Application
import com.happs.ximand.ringcontrol.BuildConfig
import com.happs.ximand.ringcontrol.model.`object`.TrustedDevice
import com.happs.ximand.ringcontrol.model.database.TrustedDevicesDatabaseHelper
import com.happs.ximand.ringcontrol.model.mapper.impl.CursorToTrustedDeviceMapper
import com.happs.ximand.ringcontrol.model.mapper.impl.TrustedDeviceToContentValuesMapper

class TrustedDeviceRepository(application: Application) : BaseRepository<TrustedDevice>() {

    override val fromCursorMapper = CursorToTrustedDeviceMapper()
    override val toContentValuesMapper = TrustedDeviceToContentValuesMapper()
    override val sqLiteHelper = TrustedDevicesDatabaseHelper(application, BuildConfig.VERSION_CODE)

    companion object {

        private var instance: TrustedDeviceRepository? = null

        fun initialize(application: Application) {
            instance = TrustedDeviceRepository(application)
        }

        fun getInstance(): TrustedDeviceRepository? {
            checkNotNull(instance) { "TrustedDevicesRepository was not initialized" }
            return instance
        }
    }

    override fun getItemId(item: TrustedDevice): Int {
        return item.id
    }

    override fun setItemId(item: TrustedDevice, id: Int) {
        item.id = id
    }
}