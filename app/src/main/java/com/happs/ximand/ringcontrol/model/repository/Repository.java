package com.happs.ximand.ringcontrol.model.repository;

import com.happs.ximand.ringcontrol.model.specification.SqlSpecification;

import java.util.List;

public interface Repository<T> {
    void add(T item);

    void update(T item);

    void remove(T item);

    List<T> query(SqlSpecification sqlSpecification);
}
