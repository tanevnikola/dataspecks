package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.builder.Builder;

public interface DynamicInvocationHandlerBuilder<T> extends
        Builder<DynamicInvocationHandler<T>>,
        DynamicInvocationHandlerBuildContract<T, DynamicInvocationHandlerBuilder<T>> {
    /**
     * Concrete builder
     * @param <T> proxy type
     * @return the builder instance
     */
    static <T> DynamicInvocationHandlerBuilder<T> create() {
        return new DynamicInvocationHandler.BuilderImpl<>();
    }
}
