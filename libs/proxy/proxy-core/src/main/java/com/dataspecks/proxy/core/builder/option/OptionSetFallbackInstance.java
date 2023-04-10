package com.dataspecks.proxy.core.builder.option;

public interface OptionSetFallbackInstance<B> extends Option {
    B setFallbackInstance(Object instance);
}
