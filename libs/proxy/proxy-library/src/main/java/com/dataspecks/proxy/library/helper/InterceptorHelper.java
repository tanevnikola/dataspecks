package com.dataspecks.proxy.library.helper;

import com.dataspecks.proxy.base.handler.interceptor.InterceptableInvocationHandler;
import com.dataspecks.proxy.base.handler.interceptor.InterceptableInvocationHandlerBuilder;

public class InterceptorHelper<T> {
    private final Class<T> type;

    public static <T> InterceptorHelper<T> forType(Class<T> type) {
        return new InterceptorHelper<>(type);
    }

    private InterceptorHelper(final Class<T> type) {
        this.type = type;
    }

    public InterceptableInvocationHandlerBuilder<T> Interceptable() {
        return InterceptableInvocationHandler.<T>builder();
    }
}
