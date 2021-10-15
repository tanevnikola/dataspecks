package com.dataspecks.proxy.core;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.Configurator;
import com.dataspecks.builder.exception.runtime.BuilderException;
import com.dataspecks.commons.exception.DException;

import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * As the name suggest: A builder for the proxy instance
 * @param <T> proxy type
 */
public class ProxyBuilder<T> implements Builder<T> {
    private final Class<T> type;
    private final Configurator<Void> configurator = new Configurator<>();
    private java.lang.reflect.InvocationHandler iHandler;

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
     * @param iHBuilder A builder that produces {@link java.lang.reflect.InvocationHandler} instance
     * @return {@link ProxyBuilder}
     */
    public ProxyBuilder<T> setHandler(Builder<? extends java.lang.reflect.InvocationHandler> iHBuilder) {
        DException.argue(Objects.nonNull(iHBuilder));
        configurator.add(unused -> {
            this.iHandler = iHBuilder.build();
            DException.argue(Objects.nonNull(this.iHandler));
        });
        return this;
    }

    /**
     * The builder's build function. Configures and creates the proxy
     *
     * @return proxy of type T
     */
    public T build() {
        DException.runOrTrow(t -> new BuilderException(
                String.format("Failed to create proxy: %s", t.getMessage()), t.getCause()),
                () -> configurator.configure(null));
        DException.argue(Objects.nonNull(iHandler), "No InvocationHandler provided");
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type}, iHandler));
    }
}
