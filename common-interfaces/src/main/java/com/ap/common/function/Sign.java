package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * The signum function of a real number x is defined as follows:
 *  sgn(x) := 
 *  -1 if  x < 0,
 *   0 if  x = 0,
 *   1 if  x > 0;
 */
public class Sign extends DifferentiableFunction {

    public OptionalDouble apply(Double net) {
        if (net > 0d)
            return OptionalDouble.of(1);
        else
            return OptionalDouble.of(-1);
    }

}
