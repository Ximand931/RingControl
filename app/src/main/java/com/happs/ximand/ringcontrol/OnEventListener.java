package com.happs.ximand.ringcontrol;

@FunctionalInterface
public interface OnEventListener<T> {
    void onEvent(T t);
}
