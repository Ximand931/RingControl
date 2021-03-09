package com.happs.ximand.ringcontrol.model.mapper

interface Mapper<FromT, ToT> {
    fun map(from: FromT): ToT
}