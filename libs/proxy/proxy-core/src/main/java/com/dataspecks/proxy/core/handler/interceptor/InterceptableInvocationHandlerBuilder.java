package com.dataspecks.proxy.core.handler.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.proxy.core.builder.contract.handler.interceptor.InterceptableInvocationHandlerBuildContract;
import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.util.Objects;

/**
 * Concrete builder
 * @param <T> proxy type
 */
public class InterceptableInvocationHandlerBuilder<T>
        extends GenericBuilder<InvocationHandler<T>, InterceptableInvocationHandler<T>>
        implements InterceptableInvocationHandlerBuildContract<T, InterceptableInvocationHandlerBuilder<T>> {

    public InterceptableInvocationHandlerBuilder() {
        super(InterceptableInvocationHandler::new);
    }

    /**
     * Set the {@link InvocationHandler} and return the builder to chain build options.
     *
     * @param iHBuilder {@link com.dataspecks.builder.Builder<InvocationHandler>}
     * @return builder
     */
    public InterceptableInvocationHandlerBuilder<T> setHandler(final Builder<? extends InvocationHandler<T>> iHBuilder) {
        DException.argue(Objects.nonNull(iHBuilder));
        configure(iIH -> iIH.setiHandler(iHBuilder.build()));
        return this;
    }

    /**
     * Set the {@link ArgumentsInterceptor} and return the builder to chain build options.
     *
     * @param aIBuilder {@link com.dataspecks.builder.Builder<ArgumentsInterceptor>}
     * @return builder
     */
    public InterceptableInvocationHandlerBuilder<T> setArgumentsInterceptor(final Builder<? extends ArgumentsInterceptor> aIBuilder) {
        DException.argue(Objects.nonNull(aIBuilder));
        configure(iIH -> iIH.setaInterceptor(aIBuilder.build()));
        return this;
    }

    /**
     * Set the {@link ResultInterceptor} and return the builder to chain build options.
     *
     * @param rInterceptor {@link ResultInterceptor<Object>}
     * @return builder
     */
    public InterceptableInvocationHandlerBuilder<T> setResultInterceptor(final ResultInterceptor<Object> rInterceptor) {
        DException.argue(Objects.nonNull(rInterceptor));
        configure(iIH -> iIH.setrInterceptor(rInterceptor));
        return this;
    }

    /**
     * Perform instance validation
     *
     * @param iIHandler {@link InterceptableInvocationHandler}
     * @return {@link InterceptableInvocationHandler}
     */
    protected InterceptableInvocationHandler<T> validate(InterceptableInvocationHandler<T> iIHandler) {
        DException.argue(Objects.nonNull(iIHandler.getiHandler()), "Invocation handler cannot be null");
        return iIHandler;
    }
}
