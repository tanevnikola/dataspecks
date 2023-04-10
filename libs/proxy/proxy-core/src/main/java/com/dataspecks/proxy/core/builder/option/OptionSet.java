package com.dataspecks.proxy.core.builder.option;

public interface OptionSet<B, T> extends Option {
    B set(T val);
}
