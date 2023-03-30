package com.dataspecks.proxy.core.handler.base.simple;

import com.dataspecks.commons.utils.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * The RedirectInvocationHandler class is a final class that implements the InvocationHandler interface.
 * It is used to redirect method calls to a specific target instance and method.
 *
 * @param <U> the type of the target instance
 */
final class RedirectInvocationHandler<U> implements InvocationHandler {

    /**
     * The target instance for method redirection.
     */
    private U targetInstance = null;

    /**
     * The target method for method redirection.
     */
    private Method targetMethod = null;

    /**
     * Gets the target instance for method redirection.
     *
     * @return the target instance
     */
    public U getTargetInstance() {
        return targetInstance;
    }

    /**
     * Sets the target instance for method redirection.
     *
     * @param targetInstance the new target instance
     */
    public void setTargetInstance(U targetInstance) {
        this.targetInstance = targetInstance;
    }

    /**
     * Gets the target method for method redirection.
     *
     * @return the target method
     */
    public Method getTargetMethod() {
        return targetMethod;
    }

    /**
     * Sets the target method for method redirection.
     *
     * @param targetMethod the new target method
     */
    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    /**
     * Redirects the method invocation to the specified target method on the
     * provided target instance, passing the given arguments.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method the method to invoke
     * @param args the arguments to pass to the method
     * @return the result of the method invocation
     * @throws Throwable if the underlying method throws an exception
     */
    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        return Methods.invoke(targetMethod, targetInstance, args);
    }
}
