package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.interceptor.ValueProducer;

public interface OptionAddArgumentComposer<B> extends Option {
    B addArgumentComposer(ValueProducer valueProducer);
}
