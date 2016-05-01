package com.ap.common.function;

import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

public class MeanSquaredError implements DoubleSupplierConsumer {
    private OptionalDouble totalSquaredErrorSum = OptionalDouble.empty();
    private double n;

    MeanSquaredError() {
    }

    public MeanSquaredError(double n) {
        this.n = n;
    }

    @Override
    public void reset() {
        totalSquaredErrorSum = OptionalDouble.empty();
    }

    @Override
    public OptionalDouble get() {
        if (totalSquaredErrorSum.isPresent()) {
            return OptionalDouble.of(totalSquaredErrorSum.getAsDouble() / n);
        } else {
            return totalSquaredErrorSum;
        }
    }

    @Override
    public void accept(DoubleStream outputError) {
        double outputErrorSqrSum = outputError.map(mapper -> mapper * mapper * 0.5).sum();
        totalSquaredErrorSum = OptionalDouble.of((totalSquaredErrorSum.isPresent() ? totalSquaredErrorSum.getAsDouble() : 0) + outputErrorSqrSum);
    }
}
