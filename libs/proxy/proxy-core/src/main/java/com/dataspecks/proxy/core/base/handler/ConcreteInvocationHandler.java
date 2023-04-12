package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.base.registry.InstanceRegistry;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ConcreteInvocationHandler<K> implements InvocationHandler {
    private final InstanceRegistry<K> instanceRegistry;
    private K cacheKey = null;
    private Method cachedMethod = null;
    private Object cachedInstance = null;

    public ConcreteInvocationHandler(InstanceRegistry<K> instanceRegistry) {
        this.instanceRegistry = instanceRegistry;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        K key = instanceRegistry.resolveKey(proxy, method, args);
        if (Objects.isNull(cacheKey) || !Objects.equals(cacheKey, key)) {
            updateCache(method, key);
        }
        return Methods.invoke(cachedInstance, cachedMethod, args);
    }

    private void updateCache(Method method, K key) throws ProxyInvocationException {
        cacheKey = key;
        if (Objects.isNull(cachedInstance = instanceRegistry.find(key))) {
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
