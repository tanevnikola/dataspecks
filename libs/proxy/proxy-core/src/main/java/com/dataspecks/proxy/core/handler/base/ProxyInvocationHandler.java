package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.proxy.core.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.handler.registry.InvocationHandlerRegistry;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ProxyInvocationHandler<K> extends DynamicInvocationHandler {

    private InvocationHandlerRegistry<K> invocationHandlerRegistry = null;

    @Override
    public Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationHandler invocationHandler = invocationHandlerRegistry.find(proxy, method, args);
        return invocationHandler.invoke(proxy, method, args);
    }

    /**
     *
     */
    public static class Builder<K> extends DynamicInvocationHandler.Builder<Builder<K>> implements
            OptionSetRegistry<Builder<K>, InvocationHandlerRegistry<K>> {

        private final ProxyInvocationHandler<K> proxyInvocationHandler;

        public Builder() {
            super(new ProxyInvocationHandler<K>());
            @SuppressWarnings("unchecked")
            ProxyInvocationHandler<K> proxyInvocationHandler = (ProxyInvocationHandler<K>) super.build();
            this.proxyInvocationHandler = proxyInvocationHandler;
        }

        @Override
        public Builder<K> setRegistry(InvocationHandlerRegistry<K> registry) {
            this.proxyInvocationHandler.invocationHandlerRegistry = registry;
            return this;
        }

        @Override
        public ProxyInvocationHandler<K> build() {
            DsExceptions.precondition(Objects.nonNull(this.proxyInvocationHandler.invocationHandlerRegistry),
                    "No invocation handler registry set.");
            return proxyInvocationHandler;
        }


    }

    public static <K> ProxyInvocationHandler.Builder<K> builder() {
        return new ProxyInvocationHandler.Builder<>();
    }
}
