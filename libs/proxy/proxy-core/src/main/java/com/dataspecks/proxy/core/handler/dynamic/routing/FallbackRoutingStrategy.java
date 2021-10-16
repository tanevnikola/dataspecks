package com.dataspecks.proxy.core.handler.dynamic.routing;

import java.lang.reflect.Method;

/**
 * This is an implementation of {@link com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy} and derived from
 * {@link AbstractRoutingStrategy} that will automatically redirect all calls from the proxy instance to the
 * appropriate (same signature) methods to the provided target instance.
 *
 * @param <T> proxy type
 * @param <U> target instance type
 */
final class FallbackRoutingStrategy<T, U> extends AbstractRoutingStrategy<T, Method> {
    private U fallbackI = null;

    /**
     * get the fallback instance
     * @return the fallback instance
     */
    public U getFallbackI() {
        return fallbackI;
    }

    /**
     * set a fallback instance
     * @param fallbackI the fallback instance to be set
     */
    public void setFallbackI(U fallbackI) {
        this.fallbackI = fallbackI;
    }

    /**
     * The {@link FallbackRoutingStrategy} is an implementation of the {@link AbstractRoutingStrategy} for route keys
     * of type {@link Method}. When a call to this method is performed, it will try simply return the called method
     * as the key.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     *
     * @return the route key - {@link Method}
     */
    @Override
    public Method getRouteKey(Object proxy, Method method, Object... args) {
        return method;
    }
}
