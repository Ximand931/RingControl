package com.happs.ximand.ringcontrol.model.mapper;

public interface Mapper<FromT, ToT> {
    ToT map(FromT from);
}
