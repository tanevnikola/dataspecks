package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.commons.exception.unchecked.UncheckedException;
import com.dataspecks.commons.reflection.Methods;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.RedirectInvocationHandlerBuilder;
import com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Concrete builder
 *
 * @param <T> proxy type
 * @param <U> target instance type
 */
public class FallbackRoutingStrategyBuilder<T, U> extends AbstractRoutingStrategyBuilder<Method> {
    private final Class<T> type;
    private final U fallbackI;
    private Boolean strictMode = true;

    public FallbackRoutingStrategyBuilder(final Class<T> type, final U fallbackI) {
        super(new FallbackRoutingStrategy());
        this.type = type;
        this.fallbackI = fallbackI;
    }

    public FallbackRoutingStrategyBuilder<T, U> setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    public BuildOptions.Set<FallbackRoutingStrategyBuilder<T, U>, Builder<? extends InvocationHandler>> forMethod(
            String name, Class<?>... args) throws ReflectionException {

        Method method = Methods.lookup(type, name, args);
        return iHBuilder -> {
            instanceTemplate.iHandlerMap.put(method, iHBuilder.build());
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
        for (Method method : type.getMethods()) {
            if (instanceTemplate.iHandlerMap.get(method) != null) {
                continue;
            }

            Method foundMethod = null;
            try {
                foundMethod = strictMode
                        ? Methods.getMatching(fallbackI.getClass(), method)
                        : Methods.findMatching(fallbackI.getClass(), method);
            } catch (ReflectionException e) {
                throw new UncheckedException(e);
            }
            if (foundMethod != null) {
                instanceTemplate.iHandlerMap.put(method, new RedirectInvocationHandlerBuilder<>(fallbackI)
                        .setMethod(foundMethod)
                        .build());
            }
        }
    }

    @Override
    public InvocationStrategy build() {
        configureMethods(type, fallbackI);
        return new FallbackRoutingStrategy(instanceTemplate);
    }

    private static final class FallbackRoutingStrategy extends AbstractRoutingStrategy<Method> {
        FallbackRoutingStrategy() {}

        FallbackRoutingStrategy(AbstractRoutingStrategy<Method> instance) {
            super(instance);
        }

        @Override
        public Method getRouteKey(Object proxy, Method method, Object... args) {
            return method;
        }
    }
}