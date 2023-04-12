package com.dataspecks.proxy.core.base.handler;

public interface InvocationInterceptor {
    Object intercept(InvocationContext ctx) throws Throwable;
}
