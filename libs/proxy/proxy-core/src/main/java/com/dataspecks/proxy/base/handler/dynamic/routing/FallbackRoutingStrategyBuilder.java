package com.dataspecks.proxy.base.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.proxy.base.builder.BuildOptions;
import com.dataspecks.proxy.base.handler.InvocationHandler;

import java.lang.reflect.Method;

/**
 *
 * @param <T> proxy type
 * @param <U> fallback instance type
 */
public interface FallbackRoutingStrategyBuilder<T, U> extends
        Builder<FallbackRoutingStrategy<T, U>>,
        RoutingStrategyBuilder<FallbackRoutingStrategy<T, U>, T, Method> {

    /**
     * The strict mode is a simple parameter that dictates the behaviour when building the the router. If the
     * strictMode is true, means that the target instance must contain all the methods from the proxy interface
     * otherwise will throw an exception during configuration.
     * @param strictMode true/false
     * @return {@link FallbackRoutingStrategyBuilder} to chain build options.
     */
    FallbackRoutingStrategyBuilder<T, U> setStrictMode(boolean strictMode);

    /**
     * Register a {@link InvocationHandler} builder from the target instance described by
     * its signature (name/args).
     * @param name method name
     * @param args method argument types
     * @return {@link BuildOptions.Set} that expects an invocation handler builder and returns the {@link FallbackRoutingStrategyBuilder}
     *
     * @throws ReflectionException in case the method cannot be found
     */
    BuildOptions.Set<FallbackRoutingStrategyBuilder<T, U>, Builder<? extends InvocationHandler<T>>> forMethod(String name, Class<?>... args) throws ReflectionException;
}
