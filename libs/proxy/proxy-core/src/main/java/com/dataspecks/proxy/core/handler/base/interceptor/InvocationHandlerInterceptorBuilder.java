package com.dataspecks.proxy.core.handler.base.interceptor;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.unchecked.DsUncheckedException;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

/**
 * Concrete builder
 */
public class InvocationHandlerInterceptorBuilder implements Builder<InvocationHandler> {
    private final InvocationHandlerInterceptor instance = new InvocationHandlerInterceptor();

    public InvocationHandlerInterceptorBuilder setHandler(InvocationHandler invocationHandler) {
        DsUncheckedException.argue(Objects.nonNull(invocationHandler));
        instance.setInvocationHandler(invocationHandler);
        return this;
    }

    public InvocationHandlerInterceptorBuilder setArgumentsInterceptor(ArgumentsInterceptor argumentsInterceptor) {
        DsUncheckedException.argue(Objects.nonNull(argumentsInterceptor));
        instance.setArgumentsInterceptor(argumentsInterceptor);
        return this;
    }

    public InvocationHandlerInterceptorBuilder setResultInterceptor(final ResultInterceptor resultInterceptor) {
        DsUncheckedException.argue(Objects.nonNull(resultInterceptor));
        instance.setResultInterceptor(resultInterceptor);
        return this;
    }

    @Override
    public InvocationHandler build() {
        return instance;
    }
}
