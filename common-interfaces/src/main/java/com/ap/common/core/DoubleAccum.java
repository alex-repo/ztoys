package com.ap.common.core;

public class DoubleAccum {

    private double value;

    public DoubleAccum(double value) {
        this.value = value;
    }

    public void add(double v) {
        value += v;
    }

    public double get() {
        return value;
    }

    public DoubleAccum sum(DoubleAccum other) {
        add(other.value);
        return this;
    }

    public void max(double other) {
        Math.max(value, other);
    }

    public void min(double other) {
        Math.min(value, other);
    }

}
