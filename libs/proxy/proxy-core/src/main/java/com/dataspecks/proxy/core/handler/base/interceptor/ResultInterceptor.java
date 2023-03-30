package com.dataspecks.proxy.core.handler.base.interceptor;

import java.lang.reflect.Method;

/**
 * Result interceptor contract. Used be the {@link InvocationHandlerInterceptor} to allow the result of the call to be
 * intercepted and/or modified. The returned result will be used as the actual invocation result
 * instead of the original.
 */
public interface ResultInterceptor {
    /**
     * Override this to intercept the result of the invocation
     *
     * @param proxy the proxy instance that the method was invoked o
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param result the actual result as returned by the {@link java.lang.reflect.InvocationHandler}
     * @param args invocation arguments
     * @return the result, to be used instead the actual result of the invocation
     * @throws Throwable ex
     */
    Object intercept(Object proxy, Method method, Object result, Object... args) throws Throwable;
}
