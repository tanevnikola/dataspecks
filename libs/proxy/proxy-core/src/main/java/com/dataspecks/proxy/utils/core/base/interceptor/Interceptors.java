package com.dataspecks.proxy.utils.core.base.interceptor;

import com.dataspecks.proxy.core.base.handler.InvocationContext;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;

public final class Interceptors {
    private Interceptors() {}

    public static final InvocationInterceptor NO_OP = InvocationContext::proceed;

}
