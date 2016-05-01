package com.ap.common.core;

public abstract class Identified<T> {

    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
