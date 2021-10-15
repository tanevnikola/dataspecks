package com.dataspecks.proxy.core.handler;

import com.dataspecks.builder.Builder;

import java.lang.reflect.Method;

/**
 * Builder contract
 *
 * @param <T> instance to be built
 * @param <U> type of the target instance to redirect to
 */
public interface RedirectInvocationHandlerBuilder<T, U> extends Builder<RedirectInvocationHandler<T, U>> {
    /**
     * Lookup a {@link Method} by its signature from the target instance and set it as target method.
     *
     * @param name method name
     * @param args method argument types
     * @return {@link RedirectInvocationHandler.BuilderImpl}
     */
    RedirectInvocationHandlerBuilder<T, U> setMethod(String name, Class<?>... args);

    /**
     * Set a {@link Method} as the target method
     *
     * @param method {@link Method} instance
     * @return {@link RedirectInvocationHandler.BuilderImpl}
     */
    RedirectInvocationHandlerBuilder<T, U> setMethod(Method method);
}
