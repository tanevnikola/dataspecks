package com.dataspecks.proxy.core.builder;

public final class BuildOptions {
    private BuildOptions() {}

    public interface Set<R, S> { R set(S s); }
}
