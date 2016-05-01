package com.ap.common.function;

import java.util.OptionalDouble;

/**
 * y = ymax, x > 0
 * y = ymin, //FIXME x <= 0
 */

public class Step extends Limited {

    public Step() {
        setYmin(0.);
        setYmax(1.);
    }

    Step(double ymin, double ymax) {
        setYmin(ymin);
        setYmax(ymax);
    }

    @Override
    public OptionalDouble apply(Double net) {
        if (net > 0.0)
            return OptionalDouble.of(getYmax());
        else
            return OptionalDouble.of(getYmin());
    }

}
