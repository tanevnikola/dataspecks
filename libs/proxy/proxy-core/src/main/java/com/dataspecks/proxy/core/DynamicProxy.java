package com.dataspecks.proxy.core;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.builder.option.*;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;
import com.dataspecks.proxy.core.handler.base.ProxyInvocationHandler;
import com.dataspecks.proxy.core.handler.registry.DefaultInstanceRegistry;
import com.dataspecks.proxy.core.handler.registry.DefaultInvocationHandlerRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy extends ProxyInvocationHandler {

    public static class Builder<T> implements
            OptionSetFallbackInstances<Builder<T>>,
            OptionForMethod<Builder.ForMethodOptions<T>>,
            OptionForMethod2<Builder.ForMethodOptions<T>>
    {
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

        @Override
        public Builder<T> setFallbackInstances(Object... instances) {
            defaultInstanceRegistryBuilder.setFallbackInstances(instances);
            return this;
        }

        @Override
        public ForMethodOptions<T> forMethod(Method m) {
           return new ForMethodOptions<>(this, m);
        }

        @Override
        public ForMethodOptions<T> forMethod(String name, Class<?>... argTypes)
                throws ReflectionException {
            Method m = Methods.lookup(proxyType, name, argTypes);
            return new ForMethodOptions<>(this, m);
        }

        public T build() {
            InvocationHandler invocationHandler = proxyInvocationHandlerBuilder
                    .setRegistry(defaultInvocationHandlerRegistryBuilder
                            .setRegistry(defaultInstanceRegistryBuilder.build())
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
                OptionIntercept<Builder<T>>,
                OptionSetFallbackInstance<Builder<T>> {

            @Override
            public Builder<T> intercept(InvocationInterceptor interceptor) {
                that.defaultInvocationHandlerRegistryBuilder
                        .forMethod(m)
                        .intercept(interceptor);
                return that;
            }

            @Override
            public Builder<T> set(InvocationHandler val) {
                that.defaultInvocationHandlerRegistryBuilder
                        .forMethod(m)
                        .set(val);
                return that;
            }

            @Override
            public Builder<T> setFallbackInstance(Object instance) {
                that.defaultInstanceRegistryBuilder
                        .forMethod(m)
                        .setFallbackInstance(instance);
                return that;
            }
        }
    }

    public static <T> Builder<T> builder(Class<T> proxyType) {
        return new Builder<>(proxyType);
    }
}
