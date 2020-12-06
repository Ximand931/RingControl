package com.happs.ximand.ringcontrol.model.repository;

import com.happs.ximand.ringcontrol.model.specification.SqlSpecification;

import java.util.List;

public interface Repository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    void remove(SqlSpecification sqlSpecification);

    List<T> query(SqlSpecification sqlSpecification);
}
