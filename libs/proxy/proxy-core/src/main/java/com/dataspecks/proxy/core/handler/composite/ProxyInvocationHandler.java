package com.dataspecks.proxy.core.handler.composite;

import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.lang.reflect.Method;

public class ProxyInvocationHandler<T> implements InvocationHandler<T> {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
