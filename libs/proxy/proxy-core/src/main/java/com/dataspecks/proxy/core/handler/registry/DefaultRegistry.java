package com.dataspecks.proxy.core.handler.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DefaultRegistry<T, K> implements Registry<T, K> {
    private final Map<K, T> registryMap = new ConcurrentHashMap<>();

    @Override
    public T find(K key) {
        return registryMap.compute(key, this::computeValue);
    }

    protected abstract T computeValue(K key, T currentValue);

    protected T registered(K key) {
        return registryMap.get(key);
    }

    protected void register(K key, T value) {
        registryMap.computeIfAbsent(key, k -> value);
    }

    protected void unregister(K key) {
        registryMap.computeIfPresent(key, (k, t) -> null);
    }
}
