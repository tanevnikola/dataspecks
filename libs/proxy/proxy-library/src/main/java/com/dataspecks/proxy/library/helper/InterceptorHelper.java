package com.dataspecks.proxy.library.helper;

import com.dataspecks.proxy.core.handler.interceptor.InterceptableInvocationHandlerBuilder;

public class InterceptorHelper<T> {
    private final Class<T> type;

    public static <T> InterceptorHelper<T> forType(Class<T> type) {
        return new InterceptorHelper<>(type);
    }

    private InterceptorHelper(final Class<T> type) {
        this.type = type;
    }

    public InterceptableInvocationHandlerBuilder<T> Interceptable() {
        return new InterceptableInvocationHandlerBuilder<>();
    }
}
