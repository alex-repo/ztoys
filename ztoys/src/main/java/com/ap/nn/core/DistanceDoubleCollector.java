package com.ap.nn.core;

import com.ap.common.core.DoubleAccum;
import com.ap.common.core.DoubleCollector;
import com.ap.common.model.Gain;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class DistanceDoubleCollector implements DoubleCollector<Sinapse> {

    @Override
    public BiConsumer<DoubleAccum, Sinapse> accumulator() {
        return (accum, sinapse) -> {
            Nucleus nucleus = sinapse.getFrom();
            Gain gain = sinapse.get();
            double diff = nucleus.getOutput() - gain.getAsDouble();
            accum.add(diff * diff);
        };
    }

    @Override
    public Function<DoubleAccum, Double> finisher() {
        return accum -> Math.sqrt(accum.get());
    }
}
