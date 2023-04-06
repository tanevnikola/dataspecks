package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;
import com.dataspecks.proxy.utils.exception.DsExceptions;
import com.dataspecks.proxy.utils.handler.InvocationHandlers;
import com.dataspecks.proxy.utils.handler.base.strategy.InvocationStrategies;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Objects;
import java.util.Optional;

/**
 * StrategyBasedInvocationHandler is an implementation of InvocationHandler that uses
 * an InvocationStrategy to determine the appropriate InvocationHandler for a
 * given method invocation.
 */
public final class InvocationStrategyHandler implements InvocationHandler {

    /**
     * The InvocationStrategy instance used to determine the appropriate
     * InvocationHandler for a given method invocation.
     */
    private InvocationStrategy strategy = InvocationStrategies.DEAD_END;

    /**
     * Returns the current InvocationStrategy instance.
     *
     * @return the current InvocationStrategy
     */
    public InvocationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Sets the InvocationStrategy instance to be used for determining the
     * appropriate InvocationHandler for a given method invocation.
     *
     * @param strategy the InvocationStrategy to be set
     */
    public void setStrategy(InvocationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Invokes the method on the proxy instance using the determined
     * InvocationHandler based on the configured InvocationStrategy.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method being invoked
     * @param args   the arguments passed to the method
     * @return the result of the method invocation
     * @throws Throwable if an error occurs during the method invocation
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationHandler handler = Optional
                .ofNullable(strategy.selectHandler(proxy, method, args))
                .orElse(InvocationHandlers.DEAD_END);
        return handler.invoke(proxy, method, args);
    }

    public static class Builder {
        private InvocationStrategyHandler instance;

        /**
         * Sets the InvocationStrategy for the StrategyBasedInvocationHandler being built.
         *
         * @param invocationStrategy the InvocationStrategy to be set
         * @return the current StrategyBasedInvocationHandlerBuilder instance
         */
        public Builder setStrategy(InvocationStrategy invocationStrategy) {
            instance.setStrategy(invocationStrategy);
            DsExceptions.argue(Objects.nonNull(instance.getStrategy()));
            return this;
        }

        /**
         * Builds the StrategyBasedInvocationHandler instance with the configured InvocationStrategy.
         *
         * @return the built StrategyBasedInvocationHandler instance
         */
        public InvocationHandler build() {
            DsExceptions.argue(Objects.nonNull(instance.getStrategy()), "No strategy set");
            return instance;
        }
    }
}
