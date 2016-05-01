package com.ap.common.model;

import java.util.function.Supplier;

public interface ResetableSupplier<R> extends Supplier<R> {
    public void reset();
}
