package com.dataspecks.proxy.core.handler.interceptor;

import com.dataspecks.builder.Builder;

public interface InterceptableInvocationHandlerBuilder<T> extends
        Builder<InterceptableInvocationHandler<T>>,
        InterceptableInvocationHandlerBuildContract<T, InterceptableInvocationHandlerBuilder<T>> {

    /**
     * Create builder instance of {@link InterceptableInvocationHandler.BuilderImpl}
     *
     * @param <T> proxy type
     * @return the builder instance
     */
    static <T> InterceptableInvocationHandlerBuilder<T> create() {
        return new InterceptableInvocationHandler.BuilderImpl<>();
    }
}
