package com.dataspecks.commons.function;

public interface UnsafeRunnable<E extends Throwable> {
    void run() throws E;
}
