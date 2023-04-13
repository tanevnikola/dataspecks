package com.dataspecks.proxy.core.extended.registry;

import com.dataspecks.proxy.builder.option.OptionAddInvocationHandlerForKey;
import com.dataspecks.proxy.builder.option.OptionInterceptForKey;
import com.dataspecks.proxy.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.base.registry.DefaultInvocationHandlerRegistry;
import com.dataspecks.proxy.core.base.registry.InstanceRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MethodInvocationHandlerRegistry extends DefaultInvocationHandlerRegistry<Method> {

    @Override
    public Method resolveKey(Object proxy, Method method, Object... args) {
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
        private final DefaultInvocationHandlerRegistry.Builder<Method> methodInvocationHandlerRegistryBuilder;

        public Builder() {
            methodInvocationHandlerRegistry = new MethodInvocationHandlerRegistry();
            methodInvocationHandlerRegistryBuilder =
                    new DefaultInvocationHandlerRegistry.Builder<>(methodInvocationHandlerRegistry);
        }

        @Override
        public Builder addInvocationHandler(Method key, InvocationHandler invocationHandler) {
            methodInvocationHandlerRegistryBuilder.addInvocationHandler(key, invocationHandler);
            return this;
        }

        @Override
        public Builder intercept(Method key, InvocationInterceptor interceptor) {
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
