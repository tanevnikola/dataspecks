package com.dataspecks.proxy.base.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.base.builder.BuildOptions;
import com.dataspecks.proxy.base.handler.InvocationHandler;

/**
 *
 * @param <I> type of the instance being built
 * @param <T> proxy type
 * @param <K> routing key type
 */
interface RoutingStrategyBuilder<I extends AbstractRoutingStrategy<T, K>, T, K> extends Builder<I> {
    BuildOptions.Set<RoutingStrategyBuilder<I, T, K>, Builder<? extends InvocationHandler<T>>> withRoute(K key);
}
