package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;

import java.lang.reflect.Method;

/**
 * The RedirectInvocationHandlerBuilder class is responsible for creating and configuring
 * RedirectInvocationHandler instances.
 *
 * @param <U> the type of the target instance
 */
public class RedirectInvocationHandlerBuilder<U> implements Builder<RedirectInvocationHandler<U>> {


    /**
     * The RedirectInvocationHandler instance being built.
     */
    private final RedirectInvocationHandler<U> handlerInstance;

    private final MethodInvocationHandlerBuilder<U> methodInvocationHandlerBuilder;

    public RedirectInvocationHandlerBuilder() {
        this.handlerInstance = new RedirectInvocationHandler<>();
        methodInvocationHandlerBuilder = new MethodInvocationHandlerBuilder<>(handlerInstance);
    }

    public RedirectInvocationHandlerBuilder(RedirectInvocationHandler<U> handlerInstance) {
        this.handlerInstance = handlerInstance;
        methodInvocationHandlerBuilder = new MethodInvocationHandlerBuilder<>(handlerInstance);
    }

    /**
     * Sets the target method.
     * <p/>
     * Note: This is a terminal function: it will return the built instance
     */
    public RedirectInvocationHandlerBuilder<U> forMethod(Method method) {
        handlerInstance.setTargetMethod(method);
        return this;
    }

    /**
     * Builds and returns the configured RedirectInvocationHandler instance.
     * <p/>
     * Note: This is a terminal function: it will return the built instance
     *
     * @return the built RedirectInvocationHandler instance
     */
    @Override
    public RedirectInvocationHandler<U> build() {
        if (handlerInstance.getTargetMethod() == null) {
            throw new IllegalArgumentException("The target method must be set before building the RedirectInvocationHandler.");
        }
        if (!handlerInstance.getTargetMethod().getDeclaringClass().equals(handlerInstance.getTargetInstance().getClass())) {
            throw new IllegalArgumentException("The provided method's declaring class must match the target instance's class.");
        }
        return handlerInstance;
    }
}

