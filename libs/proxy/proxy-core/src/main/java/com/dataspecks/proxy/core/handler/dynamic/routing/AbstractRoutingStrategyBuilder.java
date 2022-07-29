package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.exception.unchecked.DeadEndException;
import com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy;
import com.dataspecks.utils.proxy.core.handler.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract builder to build derived strategies from {@link AbstractRoutingStrategy}
 * @param <K> route key
 */
abstract class AbstractRoutingStrategyBuilder<K> implements Builder<InvocationStrategy> {
    protected final AbstractRoutingStrategy<K> instanceTemplate;

    public AbstractRoutingStrategyBuilder(AbstractRoutingStrategy<K> instanceTemplate) {
        this.instanceTemplate = instanceTemplate;
    }

    /**
     * Returns a {@link BuildOptions.Set} to be invoked with a {@link InvocationHandler} builder. This will register
     * the invocation handler to the provided route key
     *
     * @param key route key
     * @return {@link BuildOptions.Set}
     */
    public BuildOptions.Set<AbstractRoutingStrategyBuilder<K>, InvocationHandler> withRoute(K key) {
        return iHandler -> {
            instanceTemplate.iHandlerMap.put(key, iHandler);
            return this;
        };
    }

    /**
     * Abstract strategy for routing the invocations based on a route key.
     *
     * @param <K> route key
     */
    abstract static class AbstractRoutingStrategy<K> implements InvocationStrategy {
        protected final Map<K, InvocationHandler> iHandlerMap;

        AbstractRoutingStrategy() {
            this.iHandlerMap = new HashMap<>();
        }

        AbstractRoutingStrategy(AbstractRoutingStrategy<K> instance) {
            this.iHandlerMap = instance.iHandlerMap;
        }

        /**
         * Selects an invocation handler based on the route key. In case no handler is found it will return a
         * {@link InvocationHandlers#DeadEnd} handler that throws a {@link DeadEndException} when invoked
         *
         * @param proxy the proxy instance that the method was invoked on
         * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
         * @param args the arguments passed when the method was invoked
         *
         * @return selected {@link InvocationHandler} or {@link InvocationHandlers#DeadEnd} if none found
         */
        @Override
        public InvocationHandler select(Object proxy, Method method, Object... args) {
            K routeKey = getRouteKey(proxy, method, args);
            return Optional.ofNullable(routeKey)
                    .map(s -> Optional.ofNullable(iHandlerMap.get(s))
                            .orElse(InvocationHandlers.DeadEnd))
                    .orElse(InvocationHandlers.DeadEnd);
        }

        /**
         * Override this method in extended classes to resolve the route key. The route key is used by the
         * {@link AbstractRoutingStrategy#select(Object, Method, Object...)} method to find the appropriate
         * {@link InvocationHandler}
         *
         * @param proxy proxy for this call
         * @param method method that is called
         * @param args method args
         *
         * @return route key
         */
        public abstract K getRouteKey(Object proxy, Method method, Object... args);
    }

}
