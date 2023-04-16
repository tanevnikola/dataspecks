package com.dataspecks.proxy.core.extended.registry;

import com.dataspecks.proxy.builder.option.OptionAddInvocationHandlerForKey;
import com.dataspecks.proxy.builder.option.OptionInterceptForKey;
import com.dataspecks.proxy.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.base.registry.InstanceRegistry;
import com.dataspecks.proxy.core.base.registry.InvocationHandlerRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MethodInvocationHandlerRegistry extends InvocationHandlerRegistry<Method> {

    @Override
    public Method resolveKey(Object proxy, Method method, Object[] args) {
        return method;
    }

    /**
     *
     */
    public static final class Builder implements
            OptionSetRegistry<Builder, InstanceRegistry<Method>>,
            OptionAddInvocationHandlerForKey<Builder, Method>,
            OptionInterceptForKey<Builder, Method> {
        private final MethodInvocationHandlerRegistry methodInvocationHandlerRegistry;
        private final InvocationHandlerRegistry.Builder<Method> methodInvocationHandlerRegistryBuilder;

        public Builder() {
            methodInvocationHandlerRegistry = new MethodInvocationHandlerRegistry();
            methodInvocationHandlerRegistryBuilder =
                    new InvocationHandlerRegistry.Builder<>(methodInvocationHandlerRegistry);
        }

        @Override
        public Builder addInvocationHandler(Method key, InvocationHandler invocationHandler) {
            methodInvocationHandlerRegistryBuilder.addInvocationHandler(key, invocationHandler);
            return this;
        }

        @Override
        public Builder intercept(Method key, InvocationInterceptor<Method> interceptor) {
            methodInvocationHandlerRegistryBuilder.intercept(key, interceptor);
            return this;
        }

        @Override
        public Builder setRegistry(InstanceRegistry<Method> registry) {
            methodInvocationHandlerRegistryBuilder.setRegistry(registry);
            return this;
        }

        public MethodInvocationHandlerRegistry build() {
            methodInvocationHandlerRegistryBuilder.build();
            return methodInvocationHandlerRegistry;
        }
    }

    public static MethodInvocationHandlerRegistry.Builder builder() {
        return new MethodInvocationHandlerRegistry.Builder();
    }
}
