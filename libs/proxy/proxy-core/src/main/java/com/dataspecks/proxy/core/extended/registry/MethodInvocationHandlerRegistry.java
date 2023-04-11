package com.dataspecks.proxy.core.extended.registry;

import com.dataspecks.proxy.core.base.registry.DefaultInvocationHandlerRegistry;

import java.lang.reflect.Method;

public class MethodInvocationHandlerRegistry extends DefaultInvocationHandlerRegistry<Method> {

    @Override
    public Method resolveKey(Object proxy, Method method, Object... args) {
        return method;
    }

    /**
     *
     */
    public static class Builder extends DefaultInvocationHandlerRegistry.Builder<Builder, Method> {
        public Builder() {
            initialize(this, new MethodInvocationHandlerRegistry());
        }

        public MethodInvocationHandlerRegistry build() {
            return (MethodInvocationHandlerRegistry) super.build();
        }
    }

    public static MethodInvocationHandlerRegistry.Builder builder() {
        return new MethodInvocationHandlerRegistry.Builder();
    }
}
