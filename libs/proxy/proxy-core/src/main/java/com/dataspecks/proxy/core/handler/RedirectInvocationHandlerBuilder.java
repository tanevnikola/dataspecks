package com.dataspecks.proxy.core.handler;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.commons.reflection.Methods;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Build an {@link InvocationHandler} that redirects the method invocation to a target method on a target instance.
 *
 * @param <U> target instance type
 */
public class RedirectInvocationHandlerBuilder<U> implements Builder<java.lang.reflect.InvocationHandler> {

    private final RedirectInvocationHandler<U> buildingInstance = new RedirectInvocationHandler<>();

    public RedirectInvocationHandlerBuilder(U targetI) {
        DException.argue(Objects.nonNull(buildingInstance.targetInstance = targetI),
                "Target instance cannot be null");
    }

    public RedirectInvocationHandlerBuilder<U> setMethod(String name, Class<?>... args) throws ReflectionException {
        Method method = Methods.lookup(buildingInstance.targetInstance.getClass(), name, args);
        return setMethod(method);
    }

    public RedirectInvocationHandlerBuilder<U> setMethod(Method method) {
        DException.argue(method.getDeclaringClass().equals(buildingInstance.targetInstance.getClass()),
                "Provided method has declaring class that is different than the target's instance class");
        buildingInstance.targetMethod = method;
        return this;
    }

    /**
     * Perform instance validation. Both target instance and target method must no be null. Additionally the target
     * method's declaring class must be the target instance class.
     *
     * @return {@link InvocationHandler}
     */
    @Override
    public java.lang.reflect.InvocationHandler build() {
        DException.argue(Objects.nonNull(buildingInstance.targetMethod), "Target method cannot be null");
        return new RedirectInvocationHandler<>(buildingInstance);
    }

    /**
     *
     * @param <U>
     */
    private static final class RedirectInvocationHandler<U> implements java.lang.reflect.InvocationHandler {
        private U targetInstance = null;
        private Method targetMethod = null;

        RedirectInvocationHandler() {}

        RedirectInvocationHandler(RedirectInvocationHandler<U> obj) {
            this.targetInstance = obj.targetInstance;
            this.targetMethod = obj.targetMethod;
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
            return Methods.invoke(targetMethod, targetInstance, args);
        }
    }
}
