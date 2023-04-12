package com.dataspecks.proxy.utils.core.base.handler;

import com.dataspecks.proxy.exception.unchecked.NoHandlerFoundException;

import java.lang.reflect.InvocationHandler;

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

}
