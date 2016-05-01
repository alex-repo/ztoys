package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * y = (e^x - e^(-x)) / (e^x + e^(-x)) = ( e^(2*x)-1) / ( e^(2*x)+1 )
 * y'= 1 - tanh(x)^2 
 */
public class Tanh extends DifferentiableFunction {

    private double slope = 2.0;

    public Tanh() {
    }

    public Tanh(double slope) {
        this.slope = slope;
    }

    @Override
    final public OptionalDouble apply(Double x) {
        double exp2x = Math.exp(slope * x); //slope == 2 by default
        setOutput((exp2x - 1.0) / (exp2x + 1.0));
        return getOutput();
    }

    @Override
    final public OptionalDouble derivative(Double x) {
        double output = getOutput().isPresent() ? getOutput().getAsDouble() : x;
        return OptionalDouble.of(1.0 - output * output);
    }

    public double getSlope() {
        return this.slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }
}
