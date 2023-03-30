package com.dataspecks.proxy.core.handler.base.strategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationStrategy is an interface for defining strategies to select an appropriate
 * InvocationHandler based on the given proxy, method, and arguments.
 */
public interface InvocationStrategy {

    /**
     * Selects an appropriate InvocationHandler based on the provided proxy, method, and arguments.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the method being invoked
     * @param args   the arguments passed to the method
     * @return the selected InvocationHandler
     */
    InvocationHandler selectHandler(Object proxy, Method method, Object... args);
}
