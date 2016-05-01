package com.ap.nn.core;

import com.ap.common.core.DoubleAccum;
import com.ap.common.core.DoubleCollector;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public class MinDoubleCollector implements DoubleCollector<Sinapse> {

    @Override
    public Supplier<DoubleAccum> supplier() {
        return () -> new DoubleAccum(Double.MAX_VALUE);
    }

    @Override
    public BiConsumer<DoubleAccum, Sinapse> accumulator() {
        return (accum, sinapse) -> accum.min(sinapse.getWeightedInput());
    }

    @Override
    public BinaryOperator<DoubleAccum> combiner() {
        return (accum1, accum2) -> {
            accum1.min(accum2.get());
            return accum1;
        };
    }
}
