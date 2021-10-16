package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.util.function.Supplier;

/**
 * Abstract builder to build derived strategies from {@link AbstractRoutingStrategy}
 *
 * @param <I> type of the object we want to build. Must inherit {@link AbstractRoutingStrategy}
 * @param <T> proxy type
 * @param <K> route key
 */
abstract class AbstractRoutingStrategyBuilder<I extends AbstractRoutingStrategy<T, K>, T, K> extends GenericBuilder<I> {
    public AbstractRoutingStrategyBuilder(Supplier<I> instanceSupplier) {
        super(instanceSupplier);
    }

    /**
     * Returns a {@link BuildOptions.Set} to be invoked with a {@link InvocationHandler} builder. This will register
     * the invocation handler to the provided route key
     *
     * @param key route key
     * @return {@link BuildOptions.Set}
     */
    public BuildOptions.Set<AbstractRoutingStrategyBuilder<I, T, K>, Builder<? extends InvocationHandler<T>>> withRoute(K key) {
        return iHandlerBuilder -> {
            configure(aRStrategy -> aRStrategy.iHandlerMap.put(key, iHandlerBuilder.build()));
            return this;
        };
    }
}
