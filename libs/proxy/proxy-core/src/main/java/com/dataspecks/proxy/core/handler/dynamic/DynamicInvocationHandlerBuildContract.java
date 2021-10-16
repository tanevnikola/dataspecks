package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.handler.InvocationHandler;

public interface DynamicInvocationHandlerBuildContract<T, B extends Builder<? extends InvocationHandler<T>>> {

    /**
     * Set the {@link InvocationStrategy}
     *
     * @param iSBuilder {@link InvocationStrategy} builder
     * @return {@link DynamicInvocationHandlerBuilder}
     */
    B setStrategy(Builder<? extends InvocationStrategy<T>> iSBuilder);
}
