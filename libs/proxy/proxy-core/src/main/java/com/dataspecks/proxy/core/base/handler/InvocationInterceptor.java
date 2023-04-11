package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.core.base.handler.InvocationContext;

public interface InvocationInterceptor {
    Object intercept(InvocationContext ctx) throws Throwable;
}
