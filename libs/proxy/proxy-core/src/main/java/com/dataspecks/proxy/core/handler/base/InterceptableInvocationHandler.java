package com.dataspecks.proxy.core.handler.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class InterceptableInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // intercept args
        Object result = doInvoke(proxy, method, args);
        // intercept result
        return result;
    }

    public abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;
}
