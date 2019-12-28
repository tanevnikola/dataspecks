package com.dataspecks.commons.function;

public interface UnsafeConsumer<T> {
    void accept(T t) throws Throwable;
}
