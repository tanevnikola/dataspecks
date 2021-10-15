package com.dataspecks.proxy.core.handler.interceptor;

import java.lang.reflect.Method;

/**
 * Arguments interceptor
 */
public interface ArgumentsInterceptor {
    /**
     * The intercept contract. Will be called by the {@link InterceptableInvocationHandler} before the invocation
     * of its {@link java.lang.reflect.InvocationHandler}. The returned arguments will be used for the actual
     * invocation instead of the original arguments.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     * @return the arguments to be used instead of the actual arguments to perform the invocation
     * @throws Throwable ex
     */
    Object[] intercept(Object proxy, Method method, Object... args) throws Throwable;

    /**
     * A pass-through arguments interceptor (without modification of the original arguments)
     */
    ArgumentsInterceptor PassThrough = (proxy, method, args) -> args;
}
