package com.dataspecks.proxy.core;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * As the name suggest: A builder for the proxy instance
 * @param <T> proxy type
 */
public class ProxyBuilder<T> implements Builder<T> {
    private final Class<T> type;
    private InvocationHandler iHandler;

    /**
     * Constructs a {@link ProxyBuilder} for a specific interface
     * @param type the proxy type
     */
    public ProxyBuilder(final Class<T> type) {
        DException.argue(Objects.nonNull(type), "Proxy type cannot be null");
        DException.argue(type.isInterface(),
                String.format("'%s' can only be created for interfaces. '%s' is not an interface",
                        ProxyBuilder.class, type.getName()));
        this.type = type;
    }

    /**
     * Configure the instance with a {@link java.lang.reflect.InvocationHandler}
     *
     * @param iHandlerBuilder
     * @return {@link ProxyBuilder}
     */
    public ProxyBuilder<T> setHandler(Builder<? extends InvocationHandler> iHandlerBuilder) {
        DException.argue(Objects.nonNull(iHandlerBuilder), "No InvocationHandler builder provided");
        this.iHandler = iHandlerBuilder.build();
        return this;
    }

    /**
     * The builder's build function. Configures and creates the proxy
     *
     * @return proxy of type T
     */
    public T build() {
        DException.argue(Objects.nonNull(iHandler), "No InvocationHandler provided");
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type}, iHandler));
    }
}
