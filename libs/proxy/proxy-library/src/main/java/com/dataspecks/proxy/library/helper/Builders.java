package com.dataspecks.proxy.library.helper;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.base.ProxyBuilder;

public class Builders<T> {
    private final Class<T> type;
    public final InvocationHandlerHelper<T> InvocationHandler;
    public final StrategyHelper<T> Strategy;
    public final InterceptorHelper<T> Interceptor;

    public static <T> Builders<T> forType(Class<T> type) {
        return new Builders<>(type);
    }

    private Builders(final Class<T> type) {
        InvocationHandler = InvocationHandlerHelper.forType(type);
        Strategy = StrategyHelper.forType(type);
        Interceptor = InterceptorHelper.forType(type);
        this.type = type;
    }
    public ProxyBuilder<T> ProxyBuilder() {
        return new ProxyBuilder<>(type);
    }

    public ProxyBuilder<T> ProxyBuilder(Builder<? extends java.lang.reflect.InvocationHandler> iHBuilder) {
        return new ProxyBuilder<>(type).setHandler(iHBuilder);
    }

    public T Proxy(Builder<? extends java.lang.reflect.InvocationHandler> iHBuilder) {
        return ProxyBuilder(iHBuilder).build();
    }
}
