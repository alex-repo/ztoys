package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * t = x - μ
 */
public class Gaussian extends DifferentiableFunction {

    private static final double SV = 1.0 / (2.0 * Math.PI); //0.159154943092
    private static final double SS = Math.sqrt(SV); //0.398748...

    private double variance = SV;
    private double sigma = SS;

    public Gaussian() {
    }

    @Override
    public OptionalDouble apply(Double t) {
        setOutput(SS / sigma * Math.exp(-Math.pow(t, 2) / (2 * Math.pow(sigma, 2))));
        return getOutput();
    }

    @Override
    public OptionalDouble derivative(Double t) {
        double output = getOutput().isPresent() ? getOutput().getAsDouble() : apply(t).getAsDouble();
        double derivative = -t * output / variance;
        return OptionalDouble.of(derivative);
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        variance = sigma * sigma;
    }
}
