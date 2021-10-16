package com.dataspecks.proxy.core.handler;

import com.dataspecks.builder.Builder;

import java.lang.reflect.Method;

public interface RedirectInvocationHandlerBuildContract<T, B extends Builder<? extends InvocationHandler<T>>> {
    /**
     * Lookup a {@link Method} by its signature from the target instance and set it as target method.
     *
     * @param name method name
     * @param args method argument types
     * @return {@link RedirectInvocationHandlerBuilder}
     */
    B setMethod(String name, Class<?>... args);

    /**
     * Set a {@link Method} as the target method
     *
     * @param method {@link Method} instance
     * @return {@link RedirectInvocationHandlerBuilder}
     */
    B setMethod(Method method);
}
