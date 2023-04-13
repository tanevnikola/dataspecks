package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;

public interface OptionInterceptForKey<B, K> extends Option {
    B intercept(K key, InvocationInterceptor interceptor);
}
