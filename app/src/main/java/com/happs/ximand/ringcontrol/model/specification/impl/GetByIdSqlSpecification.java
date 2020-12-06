package com.happs.ximand.ringcontrol.model.specification.impl;

import com.happs.ximand.ringcontrol.model.database.TimetableDatabaseHelper;
import com.happs.ximand.ringcontrol.model.specification.SqlSpecification;

public class GetByIdSqlSpecification implements SqlSpecification {

    private long id;

    public GetByIdSqlSpecification(long id) {
        this.id = id;
    }

    @Override
    public String toSqlClauses() {
        return "WHERE " + TimetableDatabaseHelper.DB_FIELD_ID + " = " + id;
    }
}
