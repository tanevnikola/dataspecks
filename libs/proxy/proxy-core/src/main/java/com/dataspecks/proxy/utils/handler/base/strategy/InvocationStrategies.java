package com.dataspecks.proxy.utils.handler.base.strategy;

import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;
import com.dataspecks.proxy.utils.handler.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.util.function.Supplier;

/**
 * InvocationStrategies provides utility methods for creating InvocationStrategy instances.
 */
public final class InvocationStrategies {

    /**
     * Creates an InvocationStrategy that always returns the provided InvocationHandler.
     *
     * @param handler the InvocationHandler to be returned by the strategy
     * @return the created InvocationStrategy
     */
    public static InvocationStrategy handlerAsStrategy(InvocationHandler handler) {
        return (proxy, method, args) -> handler;
    }

    /**
     * Creates an InvocationStrategy that returns an InvocationHandler which supplies a constant result.
     *
     * @param resultSupplier the Supplier providing the result for the InvocationHandler
     * @return the created InvocationStrategy
     */
    public static InvocationStrategy fromResultSupplier(Supplier<Object> resultSupplier) {
        return handlerAsStrategy(InvocationHandlers.fromSupplier(resultSupplier));
    }

    /**
     * A predefined InvocationStrategy that always returns a dead-end InvocationHandler.
     */
    public static final InvocationStrategy DEAD_END = handlerAsStrategy(InvocationHandlers.DEAD_END);

    /**
     * A predefined InvocationStrategy that always returns a no-op InvocationHandler.
     */
    public static final InvocationStrategy NO_OP = handlerAsStrategy(InvocationHandlers.NO_OP);
}
