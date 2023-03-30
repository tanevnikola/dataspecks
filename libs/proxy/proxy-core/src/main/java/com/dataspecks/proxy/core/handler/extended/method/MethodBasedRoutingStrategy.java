package com.dataspecks.proxy.core.handler.extended.method;

import com.dataspecks.proxy.core.exception.unchecked.ProxyConfigurationException;
import com.dataspecks.proxy.core.handler.base.strategy.key.KeyBasedRoutingStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of {@code KeyBasedRoutingStrategy} for managing invocation handlers
 * associated with specific {@code Method} instances. This class maintains a concurrent
 * map of registered handlers and allows for efficient handler look-up and routing.
 *
 * @see KeyBasedRoutingStrategy
 */
public class MethodBasedRoutingStrategy implements KeyBasedRoutingStrategy<Method> {
    protected final Map<Method, InvocationHandler> handlerMap = new ConcurrentHashMap<>();

    /**
     * Registers an invocation handler for a given method. If a handler is already registered
     * for the specified method, a {@code ProxyConfigurationException} is thrown.
     *
     * @param key     the method for which to register the handler
     * @param handler the invocation handler to associate with the method
     * @throws ProxyConfigurationException if a duplicate handler is registered for the method
     */
    @Override
    public void registerHandler(Method key, InvocationHandler handler) throws ProxyConfigurationException {
        Objects.requireNonNull(key);
        Objects.requireNonNull(handler);
        if (handlerMap.containsKey(key)) {
            throw new ProxyConfigurationException(
                    String.format("Attempted to register a duplicate handler for key '%s'.", key.toString()));
        }
        handlerMap.put(key, handler);
    }

    /**
     * Retrieves the registered invocation handler for the specified method.
     *
     * @param key the method for which to retrieve the associated handler
     * @return the invocation handler registered for the method, or {@code null} if no handler is registered
     */
    @Override
    public InvocationHandler getHandler(Method key) {
        return handlerMap.get(key);
    }

    /**
     * Generates a route key for the provided method and its arguments. This implementation
     * uses the method itself as the route key, ignoring the proxy and method arguments.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method that was invoked
     * @param args   the arguments passed to the method
     * @return the route key, which is the method in this implementation
     */
    @Override
    public Method getRouteKey(Object proxy, Method method, Object... args) {
        return method;
    }
}
