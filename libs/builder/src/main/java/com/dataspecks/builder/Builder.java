package com.dataspecks.builder;

public interface Builder<T> {
    T build();

    /**
     * A trivial {@link Builder} that will simply pass through the instance without actually building anything. Used
     * to pass instance to functions that require {@link Builder}
     *
     * @param val the instance to be "built"
     * @param <T> instance type
     *
     * @return val - this is a simple pass-through builder
     */
    static <T> Builder<T> passThrough(T val) {
        return () -> val;
    }
}
