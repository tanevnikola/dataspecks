package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.utils.proxy.core.handler.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * The invocation strategy allows to dynamically resolve the {@link InvocationHandler} to be used for each method
 * call to the proxy. Implement this contract for custom strategies executed by the {@link DynamicInvocationHandlerBuilder}
 *
 */
public interface InvocationStrategy {

    /**
     * Selects an invocation handler to be used for the current invocation. All information about the current call
     * are provided via the arguments.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     *
     * @return selected {@link InvocationHandler} or {@link InvocationHandlers#DeadEnd} if none found
     */
    InvocationHandler select(Object proxy, Method method, Object... args);

    /**
     * Override this method if the result of the invocation should be considered for the next selection strategy
     *
     * @param result result of the execution of the currently selected {@link InvocationHandler}
     */
    default void processResult(Object result) {}

    /**
     * Constructs a trivial {@link InvocationStrategy}. It will always select the provided {@link InvocationHandler}
     *
     * @param iH {@link InvocationHandler} to select
     * @return selector that will always select the provided {@link InvocationHandler}
     */
    static InvocationStrategy trivial(InvocationHandler iH) {
        return (proxy, method, args) -> iH;
    }

    /**
     * Constructs a trivial selector {@link InvocationStrategy#trivial(InvocationHandler)} that will always
     * select a {@link InvocationHandlers#passThrough(Supplier)} for the provided {@link Supplier}
     *
     * @param resultSupplier the result supplier
     * @return strategy selector
     */
    static InvocationStrategy trivial(Supplier<Object> resultSupplier) {
        return (proxy, method, args) -> InvocationHandlers.passThrough(resultSupplier);
    }


    /**
     * Dead end strategy selector - will select a DeadEnd invocation handler
     */
    static final InvocationStrategy DeadEnd = trivial(InvocationHandlers.DeadEnd);

}
