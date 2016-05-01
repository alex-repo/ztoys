package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * V = sqrt( 2 * k * T / m)
 *
 * A = sqrt((1/pi*V)^3)
 *
 * N = 4 * pi * v^2 ??? 
 *
 * E = exp( - v^2/V )
 *
 * f(v) = N * A * E
 *
 * dN/dv = 4 * pi * N * A * E * v^2 = 4 * pi * f(v) * v^2  
 */
public class MaxwellBoltzmann extends DifferentiableFunction {

    private double V = 0.5;
    private double A = Math.pow(1 / (Math.PI * V), 3.0 / 2.0);
    private double N = 2.0;

    @Override
    public OptionalDouble apply(Double v) {
        setOutput(N * A * Math.exp(-(v * v) / V));
        return getOutput();
    }

    @Override
    public OptionalDouble derivative(Double v) {
        double output = getOutput().isPresent() ? getOutput().getAsDouble() : apply(v).getAsDouble();
        double derivative = 4 * Math.PI * output * v * v;
        return OptionalDouble.of(derivative);
    }
}
