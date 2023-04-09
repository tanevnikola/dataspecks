package com.dataspecks.proxy.core.handler.registry;

import java.lang.reflect.Method;

public interface Registry<T, K> {
    K resolveKey(Object proxy, Method method, Object... args);

    T find(K key);

    default T find(Object proxy, Method method, Object... args) {
        return find(resolveKey(proxy, method, args));
    }
}
