package com.happs.ximand.ringcontrol.model.specification.impl

import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper
import com.happs.ximand.ringcontrol.model.specification.SqlSpecification

class GetByIdSqlSpecification(private val id: Long) : SqlSpecification {
    override fun toSqlClauses(): String? {
        return "WHERE " + TimetableDatabaseHelper.DB_FIELD_ID + " = " + id
    }

}