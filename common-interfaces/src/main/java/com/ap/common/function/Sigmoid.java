package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * y = σ(x) = 1/(1+ e^(-slope*x))
 * y'= σ(x)*(1−σ(x))
 */
public class Sigmoid extends Limited {

    public Sigmoid() {
        setSlope(1.0);
    }

    public Sigmoid(double slope) {
        setSlope(slope);
    }

    @Override
    public OptionalDouble apply(Double x) {
        setOutput(1.0 / (1.0 + Math.exp(-getSlope() * x)));
        return getOutput();
    }

    @Override
    public OptionalDouble derivative(Double x) {
        double output = getOutput().isPresent() ? getOutput().getAsDouble() : apply(x).getAsDouble();
        return OptionalDouble.of(getSlope() * output * (1.0 - output));
    }

}
