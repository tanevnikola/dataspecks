package com.dataspecks.proxy.core.handler.dynamic.routing;

import com.dataspecks.builder.Builder;
import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.InvocationHandler;
import com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy;

public interface FallbackRoutingStrategyBuildContract<T, B extends Builder<? extends InvocationStrategy<T>>> {

    /**
     * The strict mode is a simple parameter that dictates the behaviour when building the the router. If the
     * strictMode is true, means that the target instance must contain all the methods from the proxy interface
     * otherwise will throw an exception during configuration.
     * @param strictMode true/false
     * @return builder
     */
    B setStrictMode(boolean strictMode);

    /**
     * Register a {@link InvocationHandler} builder from the target instance described by
     * its signature (name/args).
     * @param name method name
     * @param args method argument types
     * @return {@link BuildOptions.Set} that expects an invocation handler builder and returns the builder
     *
     * @throws ReflectionException in case the method cannot be found
     */
    BuildOptions.Set<B, Builder<? extends InvocationHandler<T>>> forMethod(String name, Class<?>... args) throws ReflectionException;
}
