package com.dataspecks.proxy.core.builder.option;

import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;

public interface OptionIntercept<B> {
    B intercept(InvocationInterceptor handler);
}
