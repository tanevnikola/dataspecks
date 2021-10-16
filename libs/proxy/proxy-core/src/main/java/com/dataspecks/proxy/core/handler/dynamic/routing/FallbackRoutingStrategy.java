package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.commons.reflection.Methods;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.InvocationHandler;
import com.dataspecks.proxy.core.handler.RedirectInvocationHandlerBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * This is an implementation of {@link com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy} and derived from
 * {@link AbstractRoutingStrategy} that will automatically redirect all calls from the proxy instance to the
 * appropriate (same signature) methods to the provided target instance.
 *
 * @param <T> proxy type
 * @param <U> target instance type
 */
final class FallbackRoutingStrategy<T, U> extends AbstractRoutingStrategy<T, Method> {
    private U fallbackI = null;

    private FallbackRoutingStrategy() {}

    /**
     * The {@link FallbackRoutingStrategy} is an implementation of the {@link AbstractRoutingStrategy} for route keys
     * of type {@link Method}. When a call to this method is performed, it will try simply return the called method
     * as the key.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     *
     * @return the route key - {@link Method}
     */
    @Override
    public Method getRouteKey(Object proxy, Method method, Object... args) {
        return method;
    }

    /**
     * Concrete builder
     *
     * @param <T> proxy type
     * @param <U> target instance type
     */
    public static class BuilderImpl<T, U> extends AbstractBuilder<FallbackRoutingStrategy<T, U>, T, Method>
        implements FallbackRoutingStrategyBuilder<T, U>{

        private final Class<T> type;
        private Boolean strictMode = false;

        public BuilderImpl(final Class<T> type, final U fallbackI) {
            super(FallbackRoutingStrategy::new);
            this.type = type;
            configureMethods(type, fallbackI);
        }

        @Override
        public BuilderImpl<T, U> setStrictMode(boolean strictMode) {
            this.strictMode = strictMode;
            return this;
        }

        @Override
        public BuildOptions.Set<
                FallbackRoutingStrategyBuilder<T, U>,
                Builder<? extends InvocationHandler<T>>
                > forMethod(String name, Class<?>... args) throws ReflectionException {

            Method method = Methods.lookup(type, name, args);
            return iHBuilder -> {
                configure(fRStrategy -> fRStrategy.iHandlerMap.put(method, iHBuilder.build()));
                return this;
            };
        }

        /**
         * Looks up for all the methods (by name and signature) from the proxy type into the fallback instance and
         * create a RedirectInvocationHandler for each.
         *
         * @param type the proxy type
         * @param fallbackI fallback instance
         */
        private void configureMethods(Class<T> type, U fallbackI) {
            DException.argue(Objects.nonNull(fallbackI));
            configure(fRStrategy -> {
                fRStrategy.fallbackI = fallbackI;
                for (Method method : type.getMethods()) {
                    Method foundMethod = strictMode
                            ? Methods.getMatching(fallbackI.getClass(), method)
                            : Methods.findMatching(fallbackI.getClass(), method);
                    if (foundMethod != null) {
                        fRStrategy.iHandlerMap.put(method, RedirectInvocationHandlerBuilder.<T, U>create(fallbackI)
                                .setMethod(foundMethod)
                                .build());
                    }
                }
            });
        }

        /**
         * Perform instance validation
         * @param fIRHandler {@link FallbackRoutingStrategy}
         * @return {@link FallbackRoutingStrategy}
         */
        @Override
        protected FallbackRoutingStrategy<T, U> validate(FallbackRoutingStrategy<T, U> fIRHandler) {
            DException.argue(Objects.nonNull(fIRHandler.fallbackI), "Target instance cannot be null");
            return super.validate(fIRHandler);
        }
    }
}
