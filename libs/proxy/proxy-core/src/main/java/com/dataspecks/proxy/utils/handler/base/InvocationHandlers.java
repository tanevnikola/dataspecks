package com.dataspecks.proxy.utils.handler.base;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.handler.registry.InstanceRegistry;
import com.dataspecks.proxy.exception.unchecked.NoHandlerFoundException;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
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

    /**
     * Returns an {@link InvocationHandler} that will lazily query the provided {@link InstanceRegistry} to find an
     * instance dedicated for this method call. Additionally, it will find the appropriate method from this instance
     * that matches the method in the proxy. If the instance or the method is not found, a {@link ProxyInvocationException}
     * will be thrown.
     * <p/>
     * It caches the matching instance and method for subsequent calls to the same method, improving performance.
     *
     * @param instanceRegistry the {@link InstanceRegistry} to query for instances
     * @return an {@link InvocationHandler} that can be used to invoke methods on instances retrieved from the registry
     * @throws ProxyInvocationException if the instance or method is not found
     */
    public static InvocationHandler fromInstanceRegistry(InstanceRegistry instanceRegistry) {
        return new InvocationHandler() {
            private Method methodToCall = null;
            private Object instance = null;
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Objects.isNull(methodToCall) || !methodToCall.equals(method)) {
                    instance = instanceRegistry.find(method);
                    if (Objects.isNull(instance)) {
                        throw new ProxyInvocationException(
                                String.format("Cannot find instance that matches method '%s'", method));
                    }

                    try {
                        methodToCall = Methods.getMatching(instance.getClass(), method);
                    } catch (ReflectionException e) {
                        throw new ProxyInvocationException(
                                String.format("Cannot find method that matches '%s' in '%s'",
                                        method, instance.getClass()), e);
                    }
                }

                return Methods.invoke(instance, methodToCall, args);
            }
        };
    }
}
