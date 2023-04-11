package com.dataspecks.proxy.builder.option;

public interface OptionSetFallbackInstance<B> extends Option {
    B setFallbackInstance(Object instance);
}
