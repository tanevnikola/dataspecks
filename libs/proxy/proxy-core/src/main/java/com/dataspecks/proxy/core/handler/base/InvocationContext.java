package com.dataspecks.proxy.core.handler.base;

import java.lang.reflect.Method;

public record InvocationContext(DynamicInvocationHandler handler, Object proxy, Method method, Object[] args) {
    public Object proceed(Object[] finalArgs) throws Throwable {
        return handler.proceed(proxy, method, finalArgs);
    }

    public Object proceed() throws Throwable {
        return handler.proceed(proxy, method, args);
    }
}
