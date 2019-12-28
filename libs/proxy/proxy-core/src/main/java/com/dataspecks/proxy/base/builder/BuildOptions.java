package com.dataspecks.proxy.base.builder;

public interface BuildOptions {
    interface Set<R, S> {
        R set(S s);
    }

    interface Pass<R, S> {
        R pass(S s);
    }
}
