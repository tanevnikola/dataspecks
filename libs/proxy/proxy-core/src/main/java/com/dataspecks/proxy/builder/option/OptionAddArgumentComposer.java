package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.interceptor.ValueComposer;

public interface OptionAddArgumentComposer<B> extends Option {
    B addArgumentComposer(ValueComposer valueComposer);
}
