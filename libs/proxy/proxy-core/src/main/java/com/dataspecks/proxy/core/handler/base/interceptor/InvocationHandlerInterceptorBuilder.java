package com.dataspecks.proxy.core.handler.base.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

/**
 * Concrete builder
 */
public class InvocationHandlerInterceptorBuilder implements Builder<InvocationHandler> {
    private final InvocationHandlerInterceptor instance = new InvocationHandlerInterceptor();

    public InvocationHandlerInterceptorBuilder setHandler(InvocationHandler invocationHandler) {
        DsExceptions.argue(Objects.nonNull(invocationHandler));
        instance.setInvocationHandler(invocationHandler);
        return this;
    }

    public InvocationHandlerInterceptorBuilder setArgumentsInterceptor(ArgumentsInterceptor argumentsInterceptor) {
        DsExceptions.argue(Objects.nonNull(argumentsInterceptor));
        instance.setArgumentsInterceptor(argumentsInterceptor);
        return this;
    }

    public InvocationHandlerInterceptorBuilder setResultInterceptor(final ResultInterceptor resultInterceptor) {
        DsExceptions.argue(Objects.nonNull(resultInterceptor));
        instance.setResultInterceptor(resultInterceptor);
        return this;
    }

    @Override
    public InvocationHandler build() {
        return instance;
    }
}
