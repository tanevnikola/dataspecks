package com.dataspecks.proxy.core.builder;

public interface BuildOptions {
    interface Set<R, S> {
        R set(S s);
    }

    interface Pass<R, S> {
        R pass(S s);
    }
}
