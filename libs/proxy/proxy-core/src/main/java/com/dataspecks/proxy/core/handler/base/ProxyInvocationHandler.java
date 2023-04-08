package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.proxy.core.handler.registry.InvocationHandlerRegistry;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ProxyInvocationHandler extends DynamicInvocationHandler {

    private InvocationHandlerRegistry invocationHandlerRegistry = null;

    @Override
    public Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationHandler invocationHandler = invocationHandlerRegistry.find(proxy, method, args);
        return invocationHandler.invoke(proxy, method, args);
    }

    /**
     *
     */
    public static class Builder extends DynamicInvocationHandler.Builder<Builder> {

        private final ProxyInvocationHandler proxyInvocationHandler;

        public Builder() {
            super(new ProxyInvocationHandler());
            this.proxyInvocationHandler = (ProxyInvocationHandler) super.build();
        }

        public Builder setInvocationHandlerRegistry(InvocationHandlerRegistry registry) {
            this.proxyInvocationHandler.invocationHandlerRegistry = registry;
            return this;
        }

        @Override
        public ProxyInvocationHandler build() {
            DsExceptions.ensure(Objects.nonNull(this.proxyInvocationHandler.invocationHandlerRegistry),
                    "No invocation handler registry set.");
            return proxyInvocationHandler;
        }
    }

    public static ProxyInvocationHandler.Builder builder() {
        return new ProxyInvocationHandler.Builder();
    }
}
