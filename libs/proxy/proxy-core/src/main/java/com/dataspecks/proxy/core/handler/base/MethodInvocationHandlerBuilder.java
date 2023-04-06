package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.builder.Builder;

import java.lang.reflect.InvocationHandler;

public class MethodInvocationHandlerBuilder<U> implements Builder<InvocationHandler> {

    private final MethodInvocationHandler<U> handlerInstance;

    public MethodInvocationHandlerBuilder() {
        this.handlerInstance = new RedirectInvocationHandler<>();
    }

    public MethodInvocationHandlerBuilder(MethodInvocationHandler<U> handlerInstance) {
        this.handlerInstance = handlerInstance;
    }

    public MethodInvocationHandlerBuilder<U> setTargetInstance(U instance) {
        if (this.handlerInstance.getTargetInstance() != null) {
            throw new IllegalArgumentException("Instance already set.");
        }
        handlerInstance.setTargetInstance(instance);
        if (this.handlerInstance.getTargetInstance() == null) {
            throw new IllegalArgumentException("The provided target instance must not be null.");
        }
        return this;
    }

    @Override
    public InvocationHandler build() {
        if (handlerInstance.getTargetInstance() == null) {
            throw new IllegalArgumentException("The target instance must be set");
        }
        return handlerInstance;
    }
}

