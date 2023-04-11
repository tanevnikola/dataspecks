package com.dataspecks.proxy.core.extended.registry;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.base.registry.Registry;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public interface InstanceRegistry<K> extends Registry<Object, K> {
    /**
     * Builds an {@link InvocationHandler} that will lazily query the {@link InstanceRegistry} to find an
     * instance dedicated for this method call. Additionally, it will find the appropriate method from this instance
     * that matches the method in the proxy. If the instance or the method is not found, a {@link ProxyInvocationException}
     * will be thrown on invocation.
     * <p/>
     * It caches the matching instance and method for subsequent calls to the same method, improving performance.
     *
     * @return an {@link InvocationHandler} that can be used to invoke methods on instances retrieved from the registry
     */
    default InvocationHandler buildInvocationHandler() {
        return new InvocationHandler() {
            private K cacheKey = null;
            private Method cachedMethod = null;
            private Object cachedInstance = null;

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                K key = resolveKey(proxy, method, args);
                if (Objects.isNull(cacheKey) || !Objects.equals(cacheKey, key)) {
                    updateCache(method, key);
                }
                return Methods.invoke(cachedInstance, cachedMethod, args);
            }

            private void updateCache(Method method, K key) throws ProxyInvocationException {
                cacheKey = key;
                if (Objects.isNull(cachedInstance = find(key))) {
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
        };
    }

}
