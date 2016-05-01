package com.ap.common.core;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface DoubleCollector<T> extends Collector<T, DoubleAccum, Double>, Serializable {

    @Override
    default Supplier<DoubleAccum> supplier() {
        return () -> new DoubleAccum(0);
    }

    @Override
    default BinaryOperator<DoubleAccum> combiner() {
        return DoubleAccum::sum;
    }

    @Override
    default Function<DoubleAccum, Double> finisher() {
        return DoubleAccum::get;
    }

    @Override
    default Set<java.util.stream.Collector.Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
