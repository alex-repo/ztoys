package com.ap.common.function;

import com.ap.common.model.ResetableSupplier;

import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;

public interface DoubleSupplierConsumer extends ResetableSupplier<OptionalDouble>, Consumer<DoubleStream> {

}
