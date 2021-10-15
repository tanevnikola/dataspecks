package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * The dynamic invocation handler uses {@link InvocationStrategy} to resolve the actual {@link InvocationHandler} that
 * will be used to handle the method call. The strategy should be provided while building the handler.
 *
 * @param <T> proxy type
 */
public final class DynamicInvocationHandler<T> implements InvocationHandler<T> {
    private InvocationStrategy<T> iStrategy = InvocationStrategy.DeadEnd();

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
        Object result = Optional.ofNullable(iStrategy.select(proxy, method, args))
                .orElse(InvocationHandler.DeadEnd())
                .invoke(proxy, method, args);
        iStrategy.processResult(result);
        return result;
    }

    /**
     * Concrete builder
     * @param <T> proxy type
     * @return the builder instance
     */
    public static <T> DynamicInvocationHandlerBuilder<T> builder() {
        return new BuilderImpl<>();
    }

    /**
     * Concrete builder
     * @param <T> proxy type
     */
    private static class BuilderImpl<T> extends GenericBuilder<DynamicInvocationHandler<T>>
            implements DynamicInvocationHandlerBuilder<T> {

        protected BuilderImpl() {
            super(DynamicInvocationHandler::new);
        }

        public DynamicInvocationHandlerBuilder<T> setStrategy(Builder<? extends InvocationStrategy<T>> iSBuilder) {
            DException.argue(Objects.nonNull(iSBuilder));
            configure(dIHandler -> dIHandler.iStrategy = iSBuilder.build());
            return this;
        }

        /**
         * Validate the instance
         * @param dIHandler {@link DynamicInvocationHandler}
         * @return {@link DynamicInvocationHandler
         */
        protected DynamicInvocationHandler<T> validate(DynamicInvocationHandler<T> dIHandler) {
            DException.argue(Objects.nonNull(dIHandler.iStrategy));
            return super.validate(dIHandler);
        }
    }
}
