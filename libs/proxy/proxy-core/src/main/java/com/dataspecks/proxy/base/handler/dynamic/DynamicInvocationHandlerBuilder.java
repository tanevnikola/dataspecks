package com.dataspecks.proxy.base.handler.dynamic;

import com.dataspecks.builder.Builder;

public interface DynamicInvocationHandlerBuilder<T> extends Builder<DynamicInvocationHandler<T>> {

    /**
     * Set the {@link InvocationStrategy}
     *
     * @param iSBuilder {@link InvocationStrategy} builder
     * @return {@link DynamicInvocationHandlerBuilder}
     */
    DynamicInvocationHandlerBuilder<T> setStrategy(Builder<? extends InvocationStrategy<T>> iSBuilder);
}
