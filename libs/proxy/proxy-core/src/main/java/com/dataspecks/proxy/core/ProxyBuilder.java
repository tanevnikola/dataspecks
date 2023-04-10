package com.dataspecks.proxy.core;

import com.dataspecks.proxy.core.handler.base.DynamicInvocationHandler;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * As the name suggest: A builder for the proxy instance
 * @param <T> proxy type
 */
public class ProxyBuilder<T> {
    private final Class<T> type;
    private DynamicInvocationHandler iHandler;

    /**
     * Constructs a {@link ProxyBuilder} for a specific interface
     * @param type the proxy type
     */
    public ProxyBuilder(final Class<T> type) {
        DsExceptions.precondition(Objects.nonNull(type), "Proxy type cannot be null");
        DsExceptions.precondition(type.isInterface(),
                String.format("'%s' can only be created for interfaces. '%s' is not an interface",
                        ProxyBuilder.class, type.getName()));
        this.type = type;
    }

    public T withHandler(DynamicInvocationHandler invocationHandler) {
        DsExceptions.precondition(Objects.nonNull(invocationHandler), "No invocation handler provided");
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
