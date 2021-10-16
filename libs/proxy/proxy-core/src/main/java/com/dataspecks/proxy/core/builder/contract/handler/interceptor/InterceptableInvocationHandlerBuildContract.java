package com.dataspecks.proxy.core.builder.contract.handler.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.handler.InvocationHandler;
import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;
import com.dataspecks.proxy.core.handler.interceptor.ResultInterceptor;

public interface InterceptableInvocationHandlerBuildContract<T, B extends Builder<? extends InvocationHandler<T>>> {

    /**
     * Set the {@link InvocationHandler}
     *
     * @param iHBuilder {@link InvocationHandler} builder
     * @return builder
     */
    B setHandler(final Builder<? extends InvocationHandler<T>> iHBuilder);

    /**
     * Set the {@link ArgumentsInterceptor}
     *
     * @param aIBuilder {@link ArgumentsInterceptor} builder
     * @return builder
     */
    B setArgumentsInterceptor(final Builder<? extends ArgumentsInterceptor> aIBuilder);

    /**
     * Set the {@link ResultInterceptor}
     *
     * @param rInterceptor {@link ResultInterceptor}
     * @return builder
     */
    B setResultInterceptor(final ResultInterceptor<Object> rInterceptor);
}
