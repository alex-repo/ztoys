package com.ap.common.function;

import java.util.OptionalDouble;

public class Ramp extends Limited {

    public Ramp() {
        this(1., 0., 1., 0., 1.);
    }

    public Ramp(double slope, double xmin, double xmax, double ymin, double ymax) {
        setSlope(slope);
        setXmin(xmin);
        setXmax(xmax);
        setYmin(ymin);
        setYmax(ymax);
    }

    @Override
    public OptionalDouble apply(Double x) {
        if (x < getXmin())
            return OptionalDouble.of(getYmin());
        else if (x > getXmax())
            return OptionalDouble.of(getYmax());
        else
            return OptionalDouble.of(getSlope() * x);
    }
}