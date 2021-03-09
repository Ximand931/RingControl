package com.happs.ximand.ringcontrol.model.specification

interface SqlSpecification {
    fun toSqlClauses(): String?
}