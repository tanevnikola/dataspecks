package com.dataspecks.proxy.core.builder.contract.handler.dynamic;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.handler.InvocationHandler;
import com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy;

public interface DynamicInvocationHandlerBuildContract<T, B extends Builder<? extends InvocationHandler<T>>> {

    /**
     * Set the {@link InvocationStrategy}
     *
     * @param iSBuilder {@link InvocationStrategy} builder
     * @return builder
     */
    B setStrategy(Builder<? extends InvocationStrategy<T>> iSBuilder);
}
