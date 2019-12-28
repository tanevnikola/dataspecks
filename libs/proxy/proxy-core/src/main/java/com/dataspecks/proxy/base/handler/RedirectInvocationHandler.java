package com.dataspecks.proxy.base.handler;

import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.reflection.Methods;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Redirects the method invocation to a target method on a target instance. The method signatures must be compatible
 *
 * @param <U> target instance type
 */
public final class RedirectInvocationHandler<T, U> implements InvocationHandler<T> {
    private U targetI = null;
    private Method targetM = null;

    private RedirectInvocationHandler() {}

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

    /**
     * Create a builder instance
     *
     * @param targetI the target instance
     * @param <T> proxy type
     * @param <U> target instanc
     *           e type
     * @return {@link BuilderImpl} instance
     */
    public static <T, U> RedirectInvocationHandlerBuilder<T, U> builder(U targetI) {
        return new BuilderImpl<>(targetI);
    }

    /**
     * Concrete builder
     * @param <T> proxy type
     * @param <U> instance type
     */
    private static class BuilderImpl<T, U> extends GenericBuilder<RedirectInvocationHandler<T, U>>
            implements RedirectInvocationHandlerBuilder<T, U> {

        private BuilderImpl(U targetI) {
            super(RedirectInvocationHandler::new);
            configure(rIHandler -> rIHandler.targetI = targetI);
        }

        public RedirectInvocationHandlerBuilder<T, U> setMethod(String name, Class<?>... args) {
            configure(rIHandler -> rIHandler.targetM = Methods.lookup(rIHandler.targetI.getClass(), name, args));
            return this;
        }

        public RedirectInvocationHandlerBuilder<T, U> setMethod(Method method) {
            configure(rIHandler -> rIHandler.targetM = method);
            return this;
        }

        /**
         * Perform instance validation. Both target instance and target method must no be null. Additionally the target
         * method's declaring class my be the target instance class.
         *
         * @param rIHandler {@link RedirectInvocationHandler}
         * @return {@link RedirectInvocationHandler}
         */
        @Override
        protected RedirectInvocationHandler<T, U> validate(RedirectInvocationHandler<T, U> rIHandler) {
            DException.argue(Objects.nonNull(rIHandler.targetI), "Target instance cannot be null");
            DException.argue(Objects.nonNull(rIHandler.targetM), "Target method cannot be null");
            DException.argue(rIHandler.targetM.getDeclaringClass().equals(rIHandler.targetI.getClass()),
                    "Target target method's declaring class is different than the target's instance class");
            return super.validate(rIHandler);
        }
    }
}
