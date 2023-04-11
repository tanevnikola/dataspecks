package com.dataspecks.proxy.core.base.registry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface InvocationHandlerRegistry<K> extends Registry<InvocationHandler, K> {

    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationHandler invocationHandler = find(proxy, method, args);
        return invocationHandler.invoke(proxy, method, args);
    }
}
