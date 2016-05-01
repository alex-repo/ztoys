package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * output = log(input)
 */
public class Log extends DifferentiableFunction {

    @Override
    public OptionalDouble apply(Double in) {
        return OptionalDouble.of(Math.log(in));
    }

    @Override
    public OptionalDouble derivative(Double in) {
        return OptionalDouble.of(1 / in);
    }

}
