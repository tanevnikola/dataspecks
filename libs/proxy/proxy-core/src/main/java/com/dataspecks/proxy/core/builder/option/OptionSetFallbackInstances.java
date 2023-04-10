package com.dataspecks.proxy.core.builder.option;

public interface OptionSetFallbackInstances<B> extends Option {
    B setFallbackInstances(Object... instances);
}
