package com.dataspecks.proxy.utils.handler.base;

import com.dataspecks.proxy.core.handler.base.InvocationContext;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;

public final class Interceptors {
    private Interceptors() {}

    public static final InvocationInterceptor NO_OP = InvocationContext::proceed;

}
