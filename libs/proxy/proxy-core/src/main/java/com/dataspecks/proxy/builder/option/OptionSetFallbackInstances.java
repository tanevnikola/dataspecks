package com.dataspecks.proxy.builder.option;

public interface OptionSetFallbackInstances<B> extends Option {
    B addFallbackInstances(Object... instances);
}
