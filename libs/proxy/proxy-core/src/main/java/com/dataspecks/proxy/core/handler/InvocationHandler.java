package com.dataspecks.proxy.core.handler;

import com.dataspecks.proxy.core.exception.unchecked.DeadEndException;

import java.util.function.Supplier;

public interface InvocationHandler<T> extends java.lang.reflect.InvocationHandler {
    /**
     * DeadEnd invocation handler
     */
    static <T> InvocationHandler<T> DeadEnd() {
        return (proxy, method, args) -> {
            throw new DeadEndException(String.format("Dead End invocation for method: '%s'", method));
        };
    }

    /**
     * Trivial invocation handler. It uses a result supplier to handle the invocation, invocation arguments are
     * ignored.
     *
     * @param resultSupplier a {@link Supplier} for the result
     * @return invocation handler
     */
    static <T, U> InvocationHandler<T> passThrough(Supplier<U> resultSupplier) {
        return (proxy, method, args) ->  resultSupplier.get();
    }

    static <T, U> InvocationHandler<T> passThrough(U result) {
        return InvocationHandler.passThrough(() ->  result);
    }
}
