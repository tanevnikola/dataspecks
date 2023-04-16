package com.dataspecks.proxy.core.base.handler;

import java.lang.reflect.Method;

public final class InvocationContext<K> {
    private final InterceptableInvocationHandler<K> handler;
    private final Object proxy;
    private final Method method;
    private final Object[] args;

    public InvocationContext(InterceptableInvocationHandler<K> handler, Object proxy, Method method, Object[] args) {
        this.handler = handler;
        this.proxy = proxy;
        this.method = method;
        this.args = args;
    }

    public Object proxy() {
        return proxy;
    }

    public Method method() {
        return method;
    }

    public Object[] args() {
        return args;
    }

    public Object proceed(Method method, Object[] args) throws Throwable {
        return handler.proceed(proxy, method, args);
    }

    public Object proceed(Object[] args) throws Throwable {
        return handler.proceed(proxy, method, args);
    }

    public Object proceed() throws Throwable {
        return handler.proceed(proxy, method, args);
    }
}
