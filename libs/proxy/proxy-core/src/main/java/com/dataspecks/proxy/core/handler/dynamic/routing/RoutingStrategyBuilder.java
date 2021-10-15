package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.InvocationHandler;

/**
 *
 * @param <I> type of the instance being built - concrete implementation of {@link AbstractRoutingStrategy}
 * @param <T> proxy type
 * @param <K> routing key type
 */
interface RoutingStrategyBuilder<I extends AbstractRoutingStrategy<T, K>, T, K> extends Builder<I> {
    BuildOptions.Set<RoutingStrategyBuilder<I, T, K>, Builder<? extends InvocationHandler<T>>> withRoute(K key);
}
