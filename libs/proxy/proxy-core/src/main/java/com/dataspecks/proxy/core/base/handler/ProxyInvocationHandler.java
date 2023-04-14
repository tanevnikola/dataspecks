package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionIntercept;
import com.dataspecks.proxy.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.base.registry.InvocationHandlerRegistry;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class ProxyInvocationHandler<K> extends InterceptableInvocationHandler<K> {

    private InvocationHandlerRegistry<K> invocationHandlerRegistry = null;

    @Override
    public Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationHandler invocationHandler = invocationHandlerRegistry.find(proxy, method, args);
        return invocationHandler.invoke(proxy, method, args);
    }

    public InvocationHandlerRegistry<K> getInvocationHandlerRegistry() {
        return invocationHandlerRegistry;
    }

    /**
     *
     */
    public static final class Builder<K> implements
            OptionSetRegistry<Builder<K>, InvocationHandlerRegistry<K>>,
            OptionIntercept<Builder<K>, K> {

        private final InterceptableInvocationHandler.Builder<K> interceptableInvocationHandlerBuilder;
        private final ProxyInvocationHandler<K> proxyInvocationHandler;

        public Builder() {
            proxyInvocationHandler = new ProxyInvocationHandler<>();
            interceptableInvocationHandlerBuilder = new InterceptableInvocationHandler.Builder<>(proxyInvocationHandler);
        }

        @Override
        public Builder<K> setRegistry(InvocationHandlerRegistry<K> registry) {
            this.proxyInvocationHandler.invocationHandlerRegistry = registry;
            return this;
        }

        @Override
        public Builder<K> intercept(InvocationInterceptor<K> interceptor) {
            interceptableInvocationHandlerBuilder.intercept(interceptor);
            return this;
        }

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
