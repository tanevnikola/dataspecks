package com.dataspecks.proxy.base.handler.interceptor;

import java.lang.reflect.Method;

/**
 * Result interceptor contract. Used be the {@link InterceptableInvocationHandler} to allow the result of the call to be
 * intercepted and/or modified. The returned result will be used as the actual invocation result
 * instead of the original.
 * @param <T> proxy type
 */
public interface ResultInterceptor<T> {
    /**
     * Override this to intercept the result of the invocation
     *
     * @param proxy the proxy instance that the method was invoked o
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param arg the actual result as returned by an {@link java.lang.reflect.InvocationHandler}
     * @return the result, to be used instead the actual result of the invocation
     * @throws Throwable ex
     */
    Object intercept(Object proxy, Method method, T arg) throws Throwable;

    /**
     * A pass-through result interceptor (without modification of the result)
     */
    ResultInterceptor<Object> PassThrough = (proxy, method, arg) -> arg;
}
