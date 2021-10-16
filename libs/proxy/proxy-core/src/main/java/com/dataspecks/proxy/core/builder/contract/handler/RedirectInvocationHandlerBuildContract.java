package com.dataspecks.proxy.core.builder.contract.handler;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.lang.reflect.Method;

public interface RedirectInvocationHandlerBuildContract<T, B extends Builder<? extends InvocationHandler<T>>> {
    /**
     * Lookup a {@link Method} by its signature from the target instance and set it as target method.
     *
     * @param name method name
     * @param args method argument types
     * @return builder
     */
    B setMethod(String name, Class<?>... args);

    /**
     * Set a {@link Method} as the target method
     *
     * @param method {@link Method} instance
     * @return builder
     */
    B setMethod(Method method);
}
