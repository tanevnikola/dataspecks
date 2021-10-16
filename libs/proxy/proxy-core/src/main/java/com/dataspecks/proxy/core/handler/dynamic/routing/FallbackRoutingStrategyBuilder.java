package com.dataspecks.proxy.core.handler.dynamic.routing;

import java.lang.reflect.Method;

/**
 * Builder
 * @param <T> proxy type
 * @param <U> fallback instance type
 */
public interface FallbackRoutingStrategyBuilder<T, U> extends
        RoutingStrategyBuilder<FallbackRoutingStrategy<T, U>, T, Method>,
        FallbackRoutingStrategyBuildContract<T, FallbackRoutingStrategyBuilder<T, U>> {

    /**
     * Returns a builder instance
     * @param type proxy type
     * @param targetI target instance type
     * @param <T> proxy type
     * @param <U> target instance type
     * @return {@link FallbackRoutingStrategy.BuilderImpl}
     */
    static <T, U> FallbackRoutingStrategyBuilder<T, U> create(Class<T> type, U targetI) {
        return new FallbackRoutingStrategy.BuilderImpl<>(type, targetI);
    }
}
