package com.dataspecks.proxy.core.base.handler;

public interface InvocationInterceptor<K> {
    Object intercept(InvocationContext<K> ctx) throws Throwable;
}
