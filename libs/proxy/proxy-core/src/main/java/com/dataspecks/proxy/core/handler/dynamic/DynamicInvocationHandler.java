package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * The dynamic invocation handler uses {@link InvocationStrategy} to resolve the actual {@link InvocationHandler} that
 * will be used to handle the method call. The strategy should be provided while building the handler.
 *
 * @param <T> proxy type
 */
final class DynamicInvocationHandler<T> implements InvocationHandler<T> {
    private InvocationStrategy<T> iStrategy = InvocationStrategy.DeadEnd();

    /**
     * get the invocation strategy
     * @return {@link InvocationStrategy}
     */
    public InvocationStrategy<T> getiStrategy() {
        return iStrategy;
    }

    /**
     * set an invocation strategy
     * @param iStrategy {@link InvocationStrategy} to be set
     */
    public void setiStrategy(InvocationStrategy<T> iStrategy) {
        this.iStrategy = iStrategy;
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
        Object result = Optional.ofNullable(iStrategy.select(proxy, method, args))
                .orElse(InvocationHandler.DeadEnd())
                .invoke(proxy, method, args);
        iStrategy.processResult(result);
        return result;
    }
}
