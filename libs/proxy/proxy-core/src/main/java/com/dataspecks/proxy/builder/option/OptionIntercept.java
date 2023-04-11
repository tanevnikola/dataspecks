package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;

public interface OptionIntercept<B> extends Option {
    B intercept(InvocationInterceptor interceptor);
}
