package com.dataspecks.proxy.core.handler.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.handler.InvocationHandler;

public interface InterceptableInvocationHandlerBuilder<T> extends Builder<InterceptableInvocationHandler<T>> {

    /**
     * Set the {@link InvocationHandler}
     *
     * @param iHBuilder {@link InvocationHandler} builder
     * @return {@link InterceptableInvocationHandlerBuilder}
     */
    InterceptableInvocationHandlerBuilder<T> setHandler(final Builder<? extends InvocationHandler<T>> iHBuilder);

    /**
     * Set the {@link ArgumentsInterceptor}
     *
     * @param aIBuilder {@link ArgumentsInterceptor} builder
     * @return {@link InterceptableInvocationHandlerBuilder}
     */
    InterceptableInvocationHandlerBuilder<T> setArgumentsInterceptor(final Builder<? extends ArgumentsInterceptor> aIBuilder);

    /**
     * Set the {@link ResultInterceptor}
     *
     * @param rInterceptor {@link ResultInterceptor}
     * @return {@link InterceptableInvocationHandlerBuilder}
     */
    InterceptableInvocationHandlerBuilder<T> setResultInterceptor(final ResultInterceptor<Object> rInterceptor);

}
