package com.ap.common.function;

import java.util.OptionalDouble;

public class Linear extends DifferentiableFunction {

    private double slope = 1.0;

    public Linear() {
    }

    public double getSlope() {
        return this.slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    @Override
    public OptionalDouble apply(Double in) {
        return OptionalDouble.of(slope * in);
    }

    @Override
    public OptionalDouble derivative(Double in) {
        return OptionalDouble.of(slope);
    }
}
