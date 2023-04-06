package com.dataspecks.proxy.core;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.utils.exception.DsExceptions;

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
        DsExceptions.argue(Objects.nonNull(type), "Proxy type cannot be null");
        DsExceptions.argue(type.isInterface(),
                String.format("'%s' can only be created for interfaces. '%s' is not an interface",
                        ProxyBuilder.class, type.getName()));
        this.type = type;
    }

    public T withHandler(InvocationHandler invocationHandler) {
        DsExceptions.argue(Objects.nonNull(invocationHandler), "No InvocationHandler provided");
        this.iHandler = invocationHandler;
        return build();
    }

    /**
     * The builder's build function. Configures and creates the proxy
     *
     * @return proxy of type T
     */
    public T build() {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type}, iHandler));
    }
}
