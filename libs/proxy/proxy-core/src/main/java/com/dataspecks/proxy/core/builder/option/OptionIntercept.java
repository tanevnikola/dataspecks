package com.dataspecks.proxy.core.builder.option;

import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;

public interface OptionIntercept<B> extends Option {
    B intercept(InvocationInterceptor interceptor);
}
