package com.dataspecks.proxy.utils.handler.base;

import com.dataspecks.proxy.exception.unchecked.NoHandlerFoundException;

import java.lang.reflect.InvocationHandler;
import java.util.function.Supplier;

/**
 * InvocationHandlers is a utility class that provides a collection of factory methods for creating
 * various types of InvocationHandler instances.
 */
public final class InvocationHandlers {
    private InvocationHandlers() {}

    /**
     * A dead-end InvocationHandler that throws a DeadEndException when invoked.
     */
    public static final InvocationHandler DEAD_END = (proxy, method, args) -> {
        String message = String.format("No handler found for method '%s'. This is a dead-end invocation.", method);
        throw new NoHandlerFoundException(message);
    };

    /**
     * An invocation handler that does nothing and returns null
     */
    public static final InvocationHandler NO_OP = (proxy, method, args) -> null;

    /**
     * Creates an InvocationHandler that returns a result from a provided Supplier.
     * Invocation arguments are ignored. The result is determined by the implementation
     * of the Supplier.
     *
     * @param resultSupplier a {@link Supplier} providing the result
     * @return an InvocationHandler that returns the result from the Supplier
     */
    public static <U> InvocationHandler fromSupplier(Supplier<U> resultSupplier) {
        return (proxy, method, args) -> resultSupplier.get();
    }

    /**
     * Creates an InvocationHandler that returns a constant result value.
     * Invocation arguments are ignored.
     *
     * @param result the constant result value
     * @return an InvocationHandler that returns the constant result value
     */
    public static <U> InvocationHandler fromValue(U result) {
        return (proxy, method, args) -> result;
    }
}
