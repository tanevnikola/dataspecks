package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.interceptor.ValueAdapter;

public interface OptionSetResultAdapter<B> extends Option {
    B setResultAdapter(ValueAdapter valueAdapter);
}
