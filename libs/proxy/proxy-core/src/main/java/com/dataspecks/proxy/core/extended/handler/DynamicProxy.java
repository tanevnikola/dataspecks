package com.dataspecks.proxy.core.extended.handler;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.builder.option.*;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.extended.registry.MethodInstanceRegistry;
import com.dataspecks.proxy.core.extended.registry.MethodInvocationHandlerRegistry;
import com.dataspecks.proxy.core.base.handler.ProxyInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy extends ProxyInvocationHandler<Method> {

    public static class Builder<T> implements
            OptionSetFallbackInstances<Builder<T>>,
            OptionForMethod<Builder.ForMethodOptions<T>>,
            OptionForMethod2<Builder.ForMethodOptions<T>> {

        private final ProxyInvocationHandler.Builder<Method> proxyInvocationHandlerBuilder;
        private final MethodInvocationHandlerRegistry.Builder methodInvocationHandlerRegistryBuilder;
        private final MethodInstanceRegistry.Builder methodInstanceRegistryBuilder;

        private final Class<T> proxyType;

        public Builder(Class<T> proxyType) {
            proxyInvocationHandlerBuilder = ProxyInvocationHandler.builder();
            methodInvocationHandlerRegistryBuilder = MethodInvocationHandlerRegistry.builder();
            methodInstanceRegistryBuilder = MethodInstanceRegistry.builder();
            this.proxyType = proxyType;
        }

        @Override
        public Builder<T> setFallbackInstances(Object... instances) {
            methodInstanceRegistryBuilder.setFallbackInstances(instances);
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
            return forMethod(m);
        }

        public T build() {
            InvocationHandler invocationHandler = proxyInvocationHandlerBuilder
                    .setRegistry(methodInvocationHandlerRegistryBuilder
                            .setRegistry(methodInstanceRegistryBuilder.build())
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
                OptionSetInvocationHandler<Builder<T>>,
                OptionIntercept<Builder<T>>,
                OptionSetFallbackInstance<Builder<T>> {

            @Override
            public Builder<T> setFallbackInstance(Object instance) {
                that.methodInstanceRegistryBuilder
                        .forMethod(m).setFallbackInstance(instance);
                return that;
            }

            @Override
            public Builder<T> intercept(InvocationInterceptor interceptor) {
                that.methodInvocationHandlerRegistryBuilder
                        .forMethod(m).intercept(interceptor);
                return that;
            }

            @Override
            public Builder<T> setInvocationHandler(InvocationHandler val) {
                that.methodInvocationHandlerRegistryBuilder
                        .forMethod(m).setInvocationHandler(val);
                return that;
            }
        }
    }

    public static <T> Builder<T> builder(Class<T> proxyType) {
        return new Builder<>(proxyType);
    }
}
