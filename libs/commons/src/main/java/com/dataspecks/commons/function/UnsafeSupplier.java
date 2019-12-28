package com.dataspecks.commons.function;

public interface UnsafeSupplier<T> {
    T get() throws Throwable;
}
