package com.dataspecks.proxy.core.handler.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.utils.proxy.core.handler.InvocationHandlers;
import com.dataspecks.utils.proxy.core.handler.dynamic.ArgumentsInterceptors;
import com.dataspecks.utils.proxy.core.handler.dynamic.ResultInterceptors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * Concrete builder
 */
public class InterceptableInvocationHandlerBuilder implements Builder<InvocationHandler> {
    private final InterceptableInvocationHandler instanceTemplate = new InterceptableInvocationHandler();

    /**
     * Set the {@link InvocationHandler} and return the builder to chain build options.
     *
     * @param iHBuilder {@link com.dataspecks.builder.Builder<InvocationHandler>}
     * @return builder
     */
    public InterceptableInvocationHandlerBuilder setHandler(final Builder<? extends InvocationHandler> iHBuilder) {
        DException.argue(Objects.nonNull(iHBuilder));
        instanceTemplate.invocationHandler = iHBuilder.build();
        return this;
    }

    /**
     * Set the {@link ArgumentsInterceptor} and return the builder to chain build options.
     *
     * @param aIBuilder {@link com.dataspecks.builder.Builder<ArgumentsInterceptor>}
     * @return builder
     */
    public InterceptableInvocationHandlerBuilder setArgumentsInterceptor(final Builder<? extends ArgumentsInterceptor> aIBuilder) {
        DException.argue(Objects.nonNull(aIBuilder));
        instanceTemplate.argumentsInterceptor = aIBuilder.build();
        return this;
    }

    /**
     * Set the {@link ResultInterceptor} and return the builder to chain build options.
     *
     * @param riBuilder {@link ResultInterceptor<Object>}
     * @return builder
     */
    public InterceptableInvocationHandlerBuilder setResultInterceptor(final Builder<ResultInterceptor<Object>> riBuilder) {
        DException.argue(Objects.nonNull(riBuilder));
        instanceTemplate.resultInterceptor = riBuilder.build();
        return this;
    }

    @Override
    public InvocationHandler build() {
        return new InterceptableInvocationHandler(instanceTemplate);
    }

    /**
     * This specialized invocation handler can be configured with {@link ArgumentsInterceptor} and {@link ResultInterceptor}
     * that will be called before/after the actual {@link InvocationHandler} invocation (respectively). The actual invocation
     * handler that is going to be invoked must be additionally provided.
     *
     * {@link ArgumentsInterceptor} result will be provided as arguments to the actual invocation and the result returned
     * by the invocation will be passed through the {@link ResultInterceptor} before returned. This allows modification of
     * the type or number of arguments for the actual invocation as well as changing the result before it is returned.
     */
    private static final class InterceptableInvocationHandler implements InvocationHandler {
        private InvocationHandler invocationHandler = InvocationHandlers.DeadEnd;
        private ArgumentsInterceptor argumentsInterceptor = null;
        private ResultInterceptor<Object> resultInterceptor = null;

        InterceptableInvocationHandler() {}

        InterceptableInvocationHandler(InterceptableInvocationHandler instance) {
            this.invocationHandler = instance.invocationHandler;
            this.argumentsInterceptor = instance.argumentsInterceptor;
            this.resultInterceptor = instance.resultInterceptor;
        }

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
            ArgumentsInterceptor aInterceptor = Optional.ofNullable(this.argumentsInterceptor)
                    .orElse(ArgumentsInterceptors.PassThrough);
            ResultInterceptor<Object> rInterceptor = Optional.ofNullable(this.resultInterceptor)
                    .orElse(ResultInterceptors.PassThrough);

            return rInterceptor.intercept(proxy, method,
                    invocationHandler.invoke(proxy, method, aInterceptor.intercept(proxy, method, args)));
        }

    }
}
