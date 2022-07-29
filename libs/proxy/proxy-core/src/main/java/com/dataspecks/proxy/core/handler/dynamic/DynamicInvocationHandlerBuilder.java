package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.utils.proxy.core.handler.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * Concrete builder
 */
public final class DynamicInvocationHandlerBuilder implements Builder<InvocationHandler> {

    private final DynamicInvocationHandler instanceTemplate = new DynamicInvocationHandler();

    public DynamicInvocationHandlerBuilder setStrategy(Builder<InvocationStrategy> invocationStrategyBuilder) {
        DException.argue(Objects.nonNull(invocationStrategyBuilder));
        instanceTemplate.setInvocationStrategy(invocationStrategyBuilder);
        return this;
    }

    @Override
    public InvocationHandler build() {
        return new DynamicInvocationHandler(instanceTemplate);
    }

    /**
     * The dynamic invocation handler uses {@link InvocationStrategy} to resolve the actual {@link InvocationHandler} that
     * will be used to handle the method call. The strategy should be provided while building the handler.
     */
    private static final class DynamicInvocationHandler implements InvocationHandler {
        private InvocationStrategy invocationStrategy = InvocationStrategy.DeadEnd;

        DynamicInvocationHandler() {}

        DynamicInvocationHandler(DynamicInvocationHandler instance) {
            this.invocationStrategy = instance.invocationStrategy;
        }

        /**
         * get the invocation strategy
         * @return {@link InvocationStrategy}
         */
        InvocationStrategy getInvocationStrategy() {
            return invocationStrategy;
        }

        /**
         * set an invocation strategy
         * @param invocationStrategyBuilder {@link InvocationStrategy} builder
         */
        void setInvocationStrategy(Builder<InvocationStrategy> invocationStrategyBuilder) {
            this.invocationStrategy = invocationStrategyBuilder.build();
        }

        /**
         * Override of the {@link InvocationHandler#invoke(Object, Method, Object[])} method that uses a strategy to resolve
         * the actual {@link java.lang.reflect.InvocationHandler} to be used
         *
         * @param proxy the proxy instance that the method was invoked on
         * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
         * @param args the arguments passed when the method was invoked
         * @return result of the invocation
         * @throws Throwable ex
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = Optional.ofNullable(invocationStrategy.select(proxy, method, args))
                    .orElse(InvocationHandlers.DeadEnd)
                    .invoke(proxy, method, args);
            invocationStrategy.processResult(result);
            return result;
        }
    }

}
