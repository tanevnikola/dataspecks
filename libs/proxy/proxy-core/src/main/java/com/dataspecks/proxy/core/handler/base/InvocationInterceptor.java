package com.dataspecks.proxy.core.handler.base;

public interface InvocationInterceptor {
    Object intercept(InvocationContext ctx) throws Throwable;
}
