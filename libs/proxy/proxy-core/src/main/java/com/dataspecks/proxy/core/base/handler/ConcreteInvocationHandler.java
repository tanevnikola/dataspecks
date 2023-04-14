package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;

import java.lang.reflect.Method;
import java.util.Objects;

public final class ConcreteInvocationHandler<K> extends SuperInvocationHandler<K> {
    private K cacheKey = null;
    private Method cachedMethod = null;
    private Object cachedInstance = null;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        K key = getInstanceRegistry(proxy).resolveKey(proxy, method, args);
        if (Objects.isNull(cacheKey) || !Objects.equals(cacheKey, key)) {
            updateCache(proxy, key, method);
        }
        return Methods.invoke(cachedInstance, cachedMethod, args);
    }

    private void updateCache(Object proxy, K key, Method method) throws ProxyInvocationException {
        cacheKey = key;
        if (Objects.isNull(cachedInstance = getInstanceRegistry(proxy).find(key))) {
            throw new ProxyInvocationException(
                    String.format("Cannot find instance that matches method '%s'", method));
        }
        try {
            cachedMethod = Methods.getMatching(cachedInstance.getClass(), method);
        } catch (ReflectionException e) {
            throw new ProxyInvocationException(
                    String.format("Cannot find method that matches '%s' in '%s'",
                            method, cachedInstance.getClass()), e);
        }
    }
}
