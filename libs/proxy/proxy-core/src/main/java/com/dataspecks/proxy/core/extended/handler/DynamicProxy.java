package com.dataspecks.proxy.core.extended.handler;

import com.dataspecks.commons.core.exception.unchecked.PreconditionViolationException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.builder.option.*;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.extended.registry.MethodInstanceRegistry;
import com.dataspecks.proxy.core.extended.registry.MethodInvocationHandlerRegistry;
import com.dataspecks.proxy.core.base.handler.ProxyInvocationHandler;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class DynamicProxy extends ProxyInvocationHandler<Method> {

    public static class Builder implements
            OptionSetFallbackInstances<Builder>,
            OptionForMethod<Builder.ForMethodOptions>,
            OptionForMethod2<Builder.ForMethodOptions> {

        private final ProxyInvocationHandler.Builder<Method> proxyInvocationHandlerBuilder;
        private final MethodInvocationHandlerRegistry.Builder methodInvocationHandlerRegistryBuilder;
        private final MethodInstanceRegistry.Builder methodInstanceRegistryBuilder;

        private final Class<?>[] interfaces;

        public Builder(Class<?>[] interfaces) {
            Objects.requireNonNull(interfaces);
            this.interfaces = interfaces.clone();
            proxyInvocationHandlerBuilder = ProxyInvocationHandler.builder();
            methodInvocationHandlerRegistryBuilder = MethodInvocationHandlerRegistry.builder();
            methodInstanceRegistryBuilder = MethodInstanceRegistry.builder();
        }

        @Override
        public Builder addFallbackInstances(Object... instances) {
            methodInstanceRegistryBuilder.addFallbackInstances(instances);
            return this;
        }

        @Override
        public ForMethodOptions forMethod(Method m) {
           return new ForMethodOptions(this, m);
        }

        @Override
        public ForMethodOptions forMethod(String name, Class<?>... argTypes) {
            Method m = Optional.ofNullable(Methods.lookup(interfaces, name, argTypes))
                    .orElseThrow(() ->
                            new PreconditionViolationException(String.format("Method not found %s", name)));
            return forMethod(m);
        }

        public <T> T build() {
            DsExceptions.precondition(interfaces.length > 0, "At least 1 interface must be provided");
            long distinct = Arrays.stream(interfaces)
                    .map(Class::getClassLoader)
                    .distinct()
                    .count();
            DsExceptions.precondition(distinct == 1, "All interfaces must be visible from the same class loader");
            @SuppressWarnings("unchecked")
            T t = (T) Proxy.newProxyInstance(
                    interfaces[0].getClassLoader(),
                    interfaces,
                    buildInvocationHandler());
            return t;
        }

        public InvocationHandler buildInvocationHandler() {
            return proxyInvocationHandlerBuilder
                    .setRegistry(methodInvocationHandlerRegistryBuilder
                            .setRegistry(methodInstanceRegistryBuilder.build())
                            .build())
                    .build();
        }

        /**
         *
         */
        public static final class ForMethodOptions implements
                OptionSetInvocationHandler<Builder>,
                OptionIntercept<Builder>,
                OptionSetFallbackInstance<Builder> {

            private final Builder that;
            private final Method m;

            public ForMethodOptions(Builder that, Method m) {
                this.that = that;
                this.m = m;
            }

            @Override
            public Builder setFallbackInstance(Object instance) {
                that.methodInstanceRegistryBuilder
                        .forMethod(m).setFallbackInstance(instance);
                return that;
            }

            @Override
            public Builder intercept(InvocationInterceptor interceptor) {
                that.methodInvocationHandlerRegistryBuilder
                        .intercept(m, interceptor);
                return that;
            }

            @Override
            public Builder setInvocationHandler(InvocationHandler val) {
                that.methodInvocationHandlerRegistryBuilder
                        .addInvocationHandler(m, val);
                return that;
            }
        }
    }

    public static  Builder builder(Class<?>... interfaces) {
        return new Builder(interfaces);
    }
}
