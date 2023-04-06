package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

/**
 * StrategyBasedInvocationHandlerBuilder is a builder for creating StrategyBasedInvocationHandler instances
 * with a specified InvocationStrategy.
 */
public final class InvocationStrategyHandlerBuilder implements Builder<InvocationHandler> {

    /**
     * The StrategyBasedInvocationHandler instance being built.
     */
    private final InvocationStrategyHandler handlerInstance = new InvocationStrategyHandler();

    /**
     * Sets the InvocationStrategy for the StrategyBasedInvocationHandler being built.
     *
     * @param invocationStrategy the InvocationStrategy to be set
     * @return the current StrategyBasedInvocationHandlerBuilder instance
     */
    public InvocationHandler fromStrategy(InvocationStrategy invocationStrategy) {
        handlerInstance.setStrategy(invocationStrategy);
        DsExceptions.argue(Objects.nonNull(handlerInstance.getStrategy()));
        return build();
    }

    /**
     * Builds the StrategyBasedInvocationHandler instance with the configured InvocationStrategy.
     *
     * @return the built StrategyBasedInvocationHandler instance
     */
    @Override
    public InvocationHandler build() {
        DsExceptions.argue(Objects.nonNull(handlerInstance.getStrategy()), "No strategy set");
        return handlerInstance;
    }
}
