package com.dataspecks.proxy.core.handler;

import com.dataspecks.commons.reflection.Methods;

import java.lang.reflect.Method;

/**
 * Redirects the method invocation to a target method on a target instance. The method signatures must be compatible
 *
 * @param <U> target instance type
 */
final class RedirectInvocationHandler<T, U> implements InvocationHandler<T> {
    private U targetI = null;
    private Method targetM = null;

    /**
     * get target instance
     * @return target instance
     */
    public U getTargetI() {
        return targetI;
    }

    /**
     * set target instance
     * @param targetI target instance
     */
    public void setTargetI(U targetI) {
        this.targetI = targetI;
    }

    /**
     * get target method
     * @return target method
     */
    public Method getTargetM() {
        return targetM;
    }

    /**
     * set target method
     * @param targetM target method
     */
    public void setTargetM(Method targetM) {
        this.targetM = targetM;
    }

    /**
     * Invokes the target method from the target instance with the provided arguments
     *
     * @param proxy the proxy instance that the method was invoked o
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     * @return result of the invocation
     * @throws Throwable ex
     */
    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
        return Methods.invoke(targetM, targetI, args);
    }


}
