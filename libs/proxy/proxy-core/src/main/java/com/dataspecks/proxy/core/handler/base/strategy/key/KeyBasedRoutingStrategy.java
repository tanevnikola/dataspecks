package com.dataspecks.proxy.core.handler.base.strategy.key;

import com.dataspecks.proxy.core.exception.unchecked.ProxyConfigurationException;
import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;
import com.dataspecks.proxy.utils.handler.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * KeyBasedRoutingStrategy is a public interface that defines a strategy for routing method invocations
 * based on a key. It extends the InvocationStrategy interface.
 *
 * @param <K> the type of key used for routing
 */
public interface KeyBasedRoutingStrategy<K> extends InvocationStrategy {

    /**
     * Selects an appropriate InvocationHandler based on the provided route key.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method being invoked
     * @param args   the arguments passed to the method
     * @return the selected InvocationHandler, or a DEAD_END handler if no route key is found
     */
    @Override
    default InvocationHandler selectHandler(Object proxy, Method method, Object... args) {
        K routeKey = getRouteKey(proxy, method, args);
        return Optional.ofNullable(routeKey)
                .map(this::getHandler)
                .orElse(InvocationHandlers.DEAD_END);
    }

    /**
     * Registers an InvocationHandler for the specified key.
     *
     * @param key      the key to associate with the given handler
     * @param handler  the InvocationHandler to register
     * @throws ProxyConfigurationException if there is an issue with the registration
     */
    void registerHandler(K key, InvocationHandler handler) throws ProxyConfigurationException;

    /**
     * Retrieves the InvocationHandler associated with the given key.
     *
     * @param key the key for which to fetch the handler
     * @return the InvocationHandler associated with the given key
     */
    InvocationHandler getHandler(K key);

    /**
     * Determines the route key for the given proxy, method, and arguments.
     * This is a mapping function F such that F(proxy, method, args) = K where K is the route key.
     *
     * Make sure that this function stays pure, meaning
     *   - Should not have side effects
     *   - It will always return the same identical result for the same input arguments
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method being invoked
     * @param args   the arguments passed to the method
     * @return the route key corresponding to the provided proxy, method, and arguments
     */
    K getRouteKey(Object proxy, Method method, Object... args);
}
