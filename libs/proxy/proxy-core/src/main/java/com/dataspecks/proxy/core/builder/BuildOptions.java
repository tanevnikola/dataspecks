package com.dataspecks.proxy.core.builder;

public final class BuildOptions {
    private BuildOptions() {}

    /**
     * A standardized build option to set a value from a builder
     *
     * @param <T> The builder that uses this build option
     * @param <U> the value to be set
     */
    public interface Set<T, U> { T set(U s); }
}
