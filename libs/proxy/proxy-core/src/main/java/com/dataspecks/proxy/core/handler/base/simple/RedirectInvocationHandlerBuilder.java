package com.dataspecks.proxy.core.handler.base.simple;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * The RedirectInvocationHandlerBuilder class is responsible for creating and configuring
 * RedirectInvocationHandler instances.
 *
 * @param <U> the type of the target instance
 */
public class RedirectInvocationHandlerBuilder<U> implements Builder<InvocationHandler> {

    /**
     * The RedirectInvocationHandler instance being built.
     */
    private final RedirectInvocationHandler<U> handlerInstance = new RedirectInvocationHandler<>();

    public RedirectInvocationHandlerBuilder(U targetInstance) {
        handlerInstance.setTargetInstance(targetInstance);
        if (handlerInstance.getTargetInstance() == null) {
            throw new IllegalArgumentException("The provided target instance must not be null.");
        }
    }

    /**
     * Sets the target method by its name and argument types.
     * <p/>
     * Note: This is a terminal function: it will return the built instance
     *
     */
    public InvocationHandler fromMethod(String name, Class<?>... args) throws ReflectionException {
        Method method = Methods.lookup(handlerInstance.getTargetInstance().getClass(), name, args);
        return fromMethod(method);
    }

    /**
     * Sets the target method.
     * <p/>
     * Note: This is a terminal function: it will return the built instance
     */
    public InvocationHandler fromMethod(Method method) {
        if (!method.getDeclaringClass().equals(handlerInstance.getTargetInstance().getClass())) {
            throw new IllegalArgumentException("The provided method's declaring class must match the target instance's class.");
        }
        handlerInstance.setTargetMethod(method);
        return build();
    }

    /**
     * Builds and returns the configured RedirectInvocationHandler instance.
     * <p/>
     * Note: This is a terminal function: it will return the built instance
     *
     * @return the built RedirectInvocationHandler instance
     */
    @Override
    public InvocationHandler build() {
        if (handlerInstance.getTargetMethod() == null) {
            throw new IllegalArgumentException("The target method must be set before building the RedirectInvocationHandler.");
        }
        return handlerInstance;
    }
}

