package com.dataspecks.proxy.base.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.base.builder.BuildOptions;
import com.dataspecks.proxy.base.exception.unchecked.DeadEndException;
import com.dataspecks.proxy.base.handler.InvocationHandler;
import com.dataspecks.proxy.base.handler.dynamic.InvocationStrategy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Abstract strategy for routing the invocations based on a route key.
 *
 * @param <T> proxy type
 * @param <K> route key
 */
abstract class AbstractRoutingStrategy<T, K> implements InvocationStrategy<T> {
    protected final Map<K, InvocationHandler<T>> iHandlerMap = new HashMap<>();

    /**
     * Selects an invocation handler based on the route key. In case no handler is found it will return a
     * {@link InvocationHandler#DeadEnd()} handler that throws a {@link DeadEndException} when invoked
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     *
     * @return selected {@link InvocationHandler} or {@link InvocationHandler#DeadEnd()} if none found
     */
    @Override
    public InvocationHandler<T> select(Object proxy, Method method, Object... args) {
        return Optional.ofNullable(getRouteKey(proxy, method, args))
                .map(s -> Optional.ofNullable(iHandlerMap.get(s))
                        .orElse(InvocationHandler.DeadEnd()))
                .orElse(InvocationHandler.DeadEnd());
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

    /**
     * Abstract builder to build derived strategies from {@link AbstractRoutingStrategy}
     *
     * @param <I> type of the object we want to build. Must inherit {@link AbstractRoutingStrategy}
     * @param <T> proxy type
     * @param <K> route key
     */
    protected static abstract class AbstractBuilder<I extends AbstractRoutingStrategy<T, K>, T, K> extends GenericBuilder<I>
            implements RoutingStrategyBuilder<I, T, K> {
        public AbstractBuilder(Supplier<I> instanceSupplier) {
            super(instanceSupplier);
        }

        /**
         * Returns a {@link BuildOptions.Set} to be invoked with a {@link InvocationHandler} builder. This will register
         * the invocation handler to the provided route key
         *
         * @param key route key
         * @return {@link BuildOptions.Set}
         */
        @Override
        public BuildOptions.Set<RoutingStrategyBuilder<I, T, K>, Builder<? extends InvocationHandler<T>>> withRoute(K key) {
            return iHandlerBuilder -> {
                configure(aRStrategy -> aRStrategy.iHandlerMap.put(key, iHandlerBuilder.build()));
                return this;
            };
        }
    }
}
