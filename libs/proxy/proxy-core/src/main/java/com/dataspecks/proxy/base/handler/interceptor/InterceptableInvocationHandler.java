package com.dataspecks.proxy.base.handler.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.proxy.base.handler.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * This specialized invocation handler can be configured with {@link ArgumentsInterceptor} and {@link ResultInterceptor}
 * that will be called before/after the actual {@link InvocationHandler} invocation (respectively). The actual invocation
 * handler that is going to be invoked must be additionally provided.
 *
 * {@link ArgumentsInterceptor} result will be provided as arguments to the actual invocation and the result returned
 * by the invocation will be passed through the {@link ResultInterceptor} before returned. This allows modification of
 * the type or number of arguments for the actual invocation as well as changing the result before it is returned.
 *
 * @param <T> proxy type
 */
public final class InterceptableInvocationHandler<T> implements InvocationHandler<T> {
    private InvocationHandler<T> iHandler = InvocationHandler.DeadEnd();
    private ArgumentsInterceptor aInterceptor = null;
    private ResultInterceptor<Object> rInterceptor = null;

    private InterceptableInvocationHandler() {}

    /**
     * Implementation of the {@link java.lang.reflect.InvocationHandler#invoke(Object, Method, Object[])} contract
     * See the class description for more info.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     *
     * @return result of the invocation
     * @throws Throwable ex
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ArgumentsInterceptor aInterceptor = Optional.ofNullable(this.aInterceptor)
                .orElse(ArgumentsInterceptor.PassThrough);
        ResultInterceptor<Object> rInterceptor = Optional.ofNullable(this.rInterceptor)
                .orElse(ResultInterceptor.PassThrough);

        return rInterceptor.intercept(proxy, method,
                iHandler.invoke(proxy, method, aInterceptor.intercept(proxy, method, args)));
    }

    /**
     * Create builder instance of {@link BuilderImpl}
     *
     * @param <T> proxy type
     * @return the builder instance
     */
    public static <T> InterceptableInvocationHandlerBuilder<T> builder() {
        return new BuilderImpl<>();
    }

    /**
     * Concrete builder
     * @param <T> proxy type
     */
    private static class BuilderImpl<T> extends GenericBuilder<InterceptableInvocationHandler<T>>
            implements InterceptableInvocationHandlerBuilder<T>{

        private BuilderImpl() {
            super(InterceptableInvocationHandler::new);
        }

        /**
         * Set the {@link InvocationHandler} and return the builder to chain build options.
         *
         * @param iHBuilder {@link com.dataspecks.builder.Builder<InvocationHandler>}
         * @return {@link BuilderImpl}
         */
        public InterceptableInvocationHandlerBuilder<T> setHandler(final Builder<? extends InvocationHandler<T>> iHBuilder) {
            DException.argue(Objects.nonNull(iHBuilder));
            configure(iIH -> iIH.iHandler = iHBuilder.build());
            return this;
        }

        /**
         * Set the {@link ArgumentsInterceptor} and return the builder to chain build options.
         *
         * @param aIBuilder {@link com.dataspecks.builder.Builder<ArgumentsInterceptor>}
         * @return {@link BuilderImpl}
         */
        public InterceptableInvocationHandlerBuilder<T> setArgumentsInterceptor(final Builder<? extends ArgumentsInterceptor> aIBuilder) {
            DException.argue(Objects.nonNull(aIBuilder));
            configure(iIH -> iIH.aInterceptor = aIBuilder.build());
            return this;
        }

        /**
         * Set the {@link ResultInterceptor} and return the builder to chain build options.
         *
         * @param rInterceptor {@link ResultInterceptor<Object>}
         * @return {@link BuilderImpl}
         */
        public InterceptableInvocationHandlerBuilder<T> setResultInterceptor(final ResultInterceptor<Object> rInterceptor) {
            DException.argue(Objects.nonNull(rInterceptor));
            configure(iIH -> iIH.rInterceptor = rInterceptor);
            return this;
        }

        /**
         * Perform instance validation
         *
         * @param iIHandler {@link InterceptableInvocationHandler}
         * @return {@link InterceptableInvocationHandler}
         */
        protected InterceptableInvocationHandler<T> validate(InterceptableInvocationHandler<T> iIHandler) {
            DException.argue(Objects.nonNull(iIHandler.iHandler), "Invocation handler cannot be null");
            return iIHandler;
        }
    }
}
