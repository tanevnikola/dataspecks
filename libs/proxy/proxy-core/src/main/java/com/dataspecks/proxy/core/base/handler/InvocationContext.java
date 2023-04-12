package com.dataspecks.proxy.core.base.handler;

import java.lang.reflect.Method;

public record InvocationContext(InterceptableInvocationHandler handler, Object proxy, Method method, Object[] args) {

    public Object proceed(Method method, Object... args) throws Throwable {
        return handler.proceed(proxy, method, args);
    }

    public Object proceed(Object[] args) throws Throwable {
        return handler.proceed(proxy, method, args);
    }

    public Object proceed() throws Throwable {
        return handler.proceed(proxy, method, args);
    }
}
