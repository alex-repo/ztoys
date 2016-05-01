package com.ap.common.function;

import java.util.OptionalDouble;

public class Sin extends DifferentiableFunction {

    @Override
    public OptionalDouble apply(Double net) {
        return OptionalDouble.of(Math.sin(net));
    }

    @Override
    public OptionalDouble derivative(Double net) {
        return OptionalDouble.of(Math.cos(net));
    }

}
