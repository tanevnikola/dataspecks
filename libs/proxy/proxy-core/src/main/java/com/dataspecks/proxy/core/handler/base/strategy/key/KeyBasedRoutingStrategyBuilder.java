package com.dataspecks.proxy.core.handler.base.strategy.key;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;

import java.lang.reflect.InvocationHandler;

/**
 * A Builder for creating derived strategies from {@link KeyBasedRoutingStrategy}.
 * @param <K> route key
 */
public class KeyBasedRoutingStrategyBuilder<K> implements Builder<InvocationStrategy> {
    private final KeyBasedRoutingStrategy<K> strategyInstance;

    public KeyBasedRoutingStrategyBuilder(KeyBasedRoutingStrategy<K> strategyInstance) {
        this.strategyInstance = strategyInstance;
    }

    protected KeyBasedRoutingStrategy<K> getInstance() {
        return strategyInstance;
    }

    /**
     * Returns a {@link BuildOptions.Set} to be invoked with an {@link InvocationHandler} builder. This will register
     * the invocation handler to the provided route key.
     *
     * @param key route key
     * @return {@link BuildOptions.Set}
     */
    public BuildOptions.Set<KeyBasedRoutingStrategyBuilder<K>, InvocationHandler> forKey(K key) {
        return invocationHandler -> {
            strategyInstance.registerHandler(key, invocationHandler);
            return this;
        };
    }

    @Override
    public InvocationStrategy build() {
        return strategyInstance;
    }
}

