package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.commons.reflection.Methods;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.builder.contract.handler.dynamic.routing.FallbackRoutingStrategyBuildContract;
import com.dataspecks.proxy.core.handler.InvocationHandler;
import com.dataspecks.proxy.core.handler.RedirectInvocationHandlerBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Concrete builder
 *
 * @param <T> proxy type
 * @param <U> target instance type
 */
public class FallbackRoutingStrategyBuilder<T, U>
        extends AbstractRoutingStrategyBuilder<FallbackRoutingStrategy<T, U>, T, Method>
        implements FallbackRoutingStrategyBuildContract<T, FallbackRoutingStrategyBuilder<T, U>>, Builder<FallbackRoutingStrategy<T, U>> {

    private final Class<T> type;
    private Boolean strictMode = false;

    public FallbackRoutingStrategyBuilder(final Class<T> type, final U fallbackI) {
        super(FallbackRoutingStrategy::new);
        this.type = type;
        configureMethods(type, fallbackI);
    }

    @Override
    public FallbackRoutingStrategyBuilder<T, U> setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    @Override
    public BuildOptions.Set<FallbackRoutingStrategyBuilder<T, U>, Builder<? extends InvocationHandler<T>>> forMethod(String name, Class<?>... args) throws ReflectionException {
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
            fRStrategy.setFallbackI(fallbackI);
            for (Method method : type.getMethods()) {
                Method foundMethod = strictMode
                        ? Methods.getMatching(fallbackI.getClass(), method)
                        : Methods.findMatching(fallbackI.getClass(), method);
                if (foundMethod != null) {
                    fRStrategy.iHandlerMap.put(method, new RedirectInvocationHandlerBuilder<T, U>(fallbackI)
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
        DException.argue(Objects.nonNull(fIRHandler.getFallbackI()), "Target instance cannot be null");
        return super.validate(fIRHandler);
    }
}