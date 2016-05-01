package com.ap.nn.core;

import com.ap.common.core.DoubleAccum;
import com.ap.common.core.DoubleCollector;

import java.util.function.BiConsumer;

public class WSumDoubleCollector implements DoubleCollector<Sinapse> {

    @Override
    public BiConsumer<DoubleAccum, Sinapse> accumulator() {
        return (accum, sinapse) -> accum.add(sinapse.getWeightedInput());
    }
}
