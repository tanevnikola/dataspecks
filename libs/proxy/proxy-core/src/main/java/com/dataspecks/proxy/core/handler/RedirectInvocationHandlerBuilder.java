package com.dataspecks.proxy.core.handler;

import com.dataspecks.builder.Builder;

/**
 * Builder
 *
 * @param <T> instance to be built
 * @param <U> type of the target instance to redirect to
 */
public interface RedirectInvocationHandlerBuilder<T, U> extends
        Builder<RedirectInvocationHandler<T, U>>,
        RedirectInvocationHandlerBuildContract<T, RedirectInvocationHandlerBuilder<T, U>> {

    /**
     * Create a builder instance
     *
     * @param targetI the target instance
     * @param <T> proxy type
     * @param <U> target instanc
     *           e type
     * @return {@link RedirectInvocationHandler.BuilderImpl} instance
     */
    static <T, U> RedirectInvocationHandlerBuilder<T, U> create(U targetI) {
        return new RedirectInvocationHandler.BuilderImpl<>(targetI);
    }
}
