package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.interceptor.ValueComposer;

public interface OptionSetValueComposer<B> extends Option {
    B setValueComposer(ValueComposer valueComposer);
}
