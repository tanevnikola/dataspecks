package com.dataspecks.proxy.core;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.builder.option.OptionIntercept;
import com.dataspecks.proxy.core.builder.option.OptionSet;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;
import com.dataspecks.proxy.core.handler.base.ProxyInvocationHandler;
import com.dataspecks.proxy.core.handler.registry.DefaultInstanceRegistry;
import com.dataspecks.proxy.core.handler.registry.DefaultInvocationHandlerRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy extends ProxyInvocationHandler {

    public static class Builder<T> {
        private final ProxyInvocationHandler.Builder proxyInvocationHandlerBuilder;
        private final DefaultInvocationHandlerRegistry.Builder defaultInvocationHandlerRegistryBuilder;
        private final DefaultInstanceRegistry.Builder defaultInstanceRegistryBuilder;
        private final Class<T> proxyType;

        public Builder(Class<T> proxyType) {
            proxyInvocationHandlerBuilder = ProxyInvocationHandler.builder();
            defaultInvocationHandlerRegistryBuilder = DefaultInvocationHandlerRegistry.builder();
            defaultInstanceRegistryBuilder = DefaultInstanceRegistry.builder();
            this.proxyType = proxyType;
        }

        public Builder<T> setFallbackInstances(Object... instances) {
            defaultInstanceRegistryBuilder.setFallbackInstances(instances);
            return this;
        }

        public ForMethodOptions<T> forMethod(Method m) {
           return new ForMethodOptions<T>(this, m);
        }

        public ForMethodOptions<T> forMethod(String name, Class<?>... argTypes)
                throws ReflectionException {
            Method m = Methods.lookup(proxyType, name, argTypes);
            return new ForMethodOptions<T>(this, m);
        }

        public T build() {
            InvocationHandler invocationHandler = proxyInvocationHandlerBuilder
                    .setInvocationHandlerRegistry(defaultInvocationHandlerRegistryBuilder
                            .setFallbackRegistry(defaultInstanceRegistryBuilder
                                    .build())
                            .build())
                    .build();
            return proxyType.cast(Proxy.newProxyInstance(
                    proxyType.getClassLoader(),
                    new Class[] { proxyType },
                    invocationHandler));
        }

        /**
         *
         * @param <T>
         */
        public record ForMethodOptions<T>(Builder<T> that, Method m) implements
                OptionSet<Builder<T>, InvocationHandler>,
                OptionIntercept<Builder<T>> {

            @Override
            public Builder<T> intercept(InvocationInterceptor handler) {
                that.defaultInvocationHandlerRegistryBuilder
                        .forMethod(m).intercept(handler);
                return that;
            }

            @Override
            public Builder<T> set(InvocationHandler val) {
                that.defaultInvocationHandlerRegistryBuilder
                        .forMethod(m).set(val);
                return that;
            }
        }
    }

    public static <T> Builder<T> builder(Class<T> proxyType) {
        return new Builder<>(proxyType);
    }
}
