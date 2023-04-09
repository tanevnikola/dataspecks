package com.dataspecks.proxy.utils.handler.base;

import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.handler.base.DynamicInvocationHandler;
import com.dataspecks.proxy.exception.unchecked.NoHandlerFoundException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * InvocationHandlers is a utility class that provides a collection of factory methods for creating
 * various types of InvocationHandler instances.
 */
public final class DynamicInvocationHandlers {
    private DynamicInvocationHandlers() {}

    /**
     * A dead-end InvocationHandler that throws a DeadEndException when invoked.
     */
    public static final DynamicInvocationHandler DEAD_END = new DynamicInvocationHandler() {
        @Override
        protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
            String message = String.format("No handler found for method '%s'. This is a dead-end invocation.", method);
            throw new NoHandlerFoundException(message);
        }
    };

    /**
     * An invocation handler that does nothing and returns null
     */
    public static final DynamicInvocationHandler NO_OP = new DynamicInvocationHandler() {
        @Override
        protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    };

    /**
     * Creates an InvocationHandler that returns a result from a provided Supplier.
     * Invocation arguments are ignored. The result is determined by the implementation
     * of the Supplier.
     *
     * @param resultSupplier a {@link Supplier} providing the result
     * @return an InvocationHandler that returns the result from the Supplier
     */
    public static <U> DynamicInvocationHandler fromSupplier(Supplier<U> resultSupplier) {
        return new DynamicInvocationHandler() {
            @Override
            protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
                return resultSupplier.get();
            }
        };
    }

    /**
     * Creates an InvocationHandler that returns a constant result value.
     * Invocation arguments are ignored.
     *
     * @param result the constant result value
     * @return an InvocationHandler that returns the constant result value
     */
    public static <U> DynamicInvocationHandler fromValue(U result) {
        return new DynamicInvocationHandler() {
            @Override
            protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
                return result;
            }
        };
    }

    /**
     *
     * @param instance
     * @param methodToCall
     * @return
     */
    public static DynamicInvocationHandler fromMethodCall(Object instance, Method methodToCall) {
        return new DynamicInvocationHandler() {
            @Override
            protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
                return Methods.invoke(instance, methodToCall, args);
            }
        };
    }

    /**
     *
     * @param invocationHandler
     * @return
     */
    public static DynamicInvocationHandler fromInvocationHandler(InvocationHandler invocationHandler) {
        return new DynamicInvocationHandler() {
            @Override
            protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
                return invocationHandler.invoke(proxy, method, args);
            }
        };
    }
}
