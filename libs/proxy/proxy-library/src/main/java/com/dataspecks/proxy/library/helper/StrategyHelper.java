package com.dataspecks.proxy.library.helper;

import com.dataspecks.proxy.core.handler.dynamic.routing.FallbackRoutingStrategyBuilder;

public class StrategyHelper<T> {
    private final Class<T> type;

    public static <T> StrategyHelper<T> forType(Class<T> type) {
        return new StrategyHelper<>(type);
    }

    private StrategyHelper(final Class<T> type) {
        this.type = type;
    }

    public <U> FallbackRoutingStrategyBuilder<T, U> Fallback(U target) {
        return FallbackRoutingStrategyBuilder.create(type, target);
    }

}
