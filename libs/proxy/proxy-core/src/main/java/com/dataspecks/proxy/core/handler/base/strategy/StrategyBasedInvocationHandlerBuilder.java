package com.dataspecks.proxy.core.handler.base.strategy;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.unchecked.DsUncheckedException;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

/**
 * StrategyBasedInvocationHandlerBuilder is a builder for creating StrategyBasedInvocationHandler instances
 * with a specified InvocationStrategy.
 */
public final class StrategyBasedInvocationHandlerBuilder implements Builder<InvocationHandler> {

    /**
     * The StrategyBasedInvocationHandler instance being built.
     */
    private final StrategyBasedInvocationHandler handlerInstance = new StrategyBasedInvocationHandler();

    /**
     * Sets the InvocationStrategy for the StrategyBasedInvocationHandler being built.
     *
     * @param invocationStrategy the InvocationStrategy to be set
     * @return the current StrategyBasedInvocationHandlerBuilder instance
     */
    public InvocationHandler fromStrategy(InvocationStrategy invocationStrategy) {
        handlerInstance.setStrategy(invocationStrategy);
        DsUncheckedException.argue(Objects.nonNull(handlerInstance.getStrategy()));
        return build();
    }

    /**
     * Builds the StrategyBasedInvocationHandler instance with the configured InvocationStrategy.
     *
     * @return the built StrategyBasedInvocationHandler instance
     */
    @Override
    public InvocationHandler build() {
        DsUncheckedException.argue(Objects.nonNull(handlerInstance.getStrategy()), "No strategy set");
        return handlerInstance;
    }
}
