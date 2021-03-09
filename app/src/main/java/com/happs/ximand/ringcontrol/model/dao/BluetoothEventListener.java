package com.happs.ximand.ringcontrol.model.dao;

import com.happs.ximand.ringcontrol.OnEventListener;

import java.util.Objects;
import java.util.Random;

@Deprecated
public final class BluetoothEventListener<T> implements OnEventListener<T> {

    private final long id;
    private final OnEventListener<T> eventListener;

    public BluetoothEventListener(OnEventListener<T> eventListener) {
        this.id = new Random().nextLong();
        this.eventListener = eventListener;
    }

    @Override
    public void onEvent(T t) {
        eventListener.onEvent(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothEventListener<?> that = (BluetoothEventListener<?>) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
