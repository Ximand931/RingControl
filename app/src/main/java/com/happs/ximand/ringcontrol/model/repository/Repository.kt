package com.happs.ximand.ringcontrol.model.repository

import com.happs.ximand.ringcontrol.model.specification.SqlSpecification

interface Repository<T> {
    fun add(item: T)
    fun update(item: T)
    fun remove(item: T)
    fun query(sqlSpecification: SqlSpecification): List<T>
}