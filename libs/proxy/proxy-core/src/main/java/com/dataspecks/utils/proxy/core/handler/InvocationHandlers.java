package com.dataspecks.utils.proxy.core.handler;

import com.dataspecks.proxy.core.exception.unchecked.DeadEndException;

import java.lang.reflect.InvocationHandler;
import java.util.function.Supplier;

public final class InvocationHandlers {
    private InvocationHandlers() {};

    /**
     * DeadEnd invocation handler
     */
    public static final InvocationHandler DeadEnd = (proxy, method, args) -> {
        throw new DeadEndException(String.format("Dead End invocation for method: '%s'", method));
    };

    /**
     * Trivial invocation handler. It uses a result supplier to handle the invocation, invocation arguments are
     * ignored.
     *
     * @param resultSupplier a {@link Supplier} for the result
     * @return invocation handler
     */
    public static <U> InvocationHandler passThrough(Supplier<U> resultSupplier) {
        return (proxy, method, args) ->  resultSupplier.get();
    }

    public static <U> InvocationHandler passThrough(U result) {
        return (proxy, method, args) ->  result;
    }
}
