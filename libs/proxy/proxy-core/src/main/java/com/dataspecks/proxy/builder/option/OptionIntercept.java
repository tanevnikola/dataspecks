package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;

public interface OptionIntercept<B, K> extends Option {
    B intercept(InvocationInterceptor<K> interceptor);
}
