package com.dataspecks.proxy.core.handler.extended.method;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.exception.unchecked.ProxyConfigurationException;
import com.dataspecks.proxy.core.handler.base.InterceptableInvocationHandler;
import com.dataspecks.proxy.core.handler.base.RedirectInvocationHandlerBuilder;
import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;
import com.dataspecks.proxy.core.handler.base.strategy.key.KeyBasedRoutingStrategyBuilder;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A builder class for creating and configuring instances of {@code MethodBasedRoutingStrategy}.
 * This builder is responsible for both configuration and instantiation of the strategy. It allows for the
 * registration of invocation handlers for specific methods and provides a fallback instance to handle methods
 * that are not explicitly registered. The behavior of the builder may be customized through various configuration
 * options, such as method matching rules and fallback handling.
 *
 * @param <T> the interface type to create a dynamic proxy for
 */
public class MethodImplementationStrategyBuilder<T> extends KeyBasedRoutingStrategyBuilder<Method> {

    private final Class<T> interfaceType;
    private final List<Object> fallbackInstances = new ArrayList<>();
    private Boolean strictMode = true;

    /**
     * Constructs a new {@code MethodBasedRoutingStrategyBuilder} with the given interface type and fallback instance.
     *
     * @param interfaceType   the interface type to create a dynamic proxy for
     */
    public MethodImplementationStrategyBuilder(final Class<T> interfaceType) {
        super(new MethodImplementationStrategy());
        this.interfaceType = interfaceType;
    }

    public MethodImplementationStrategyBuilder<T> addFallbackInstance(Object instance) {
        fallbackInstances.add(instance);
        return this;
    }

    /**
     * Configures the strict mode for the builder.
     * <p/>
     * When set to {@code true}, the builder requires either a registered handler for each method or an exact method
     * signature match between the interface and fallback instance for those methods that do not have a registered
     * handler. It will throw an exception if no matching method is found.
     * <p/>
     * When set to {@code false}, the builder does not enforce that each method from the interface has a counterpart.
     * Note that if a specific method has no registered handler and has no matching "fallback" method then if this
     * method is invoked it will invoke a InvocationHandlers.DEAD_END handler and simply throw a
     * {@link com.dataspecks.proxy.core.exception.unchecked.DeadEndException}
     * <p/>
     * The strict mode is considered during the build process, and any exceptions related to the strict mode setting
     * are thrown while building and configuring the instance.
     *
     * @param strictMode the strict mode setting for the builder
     * @return this builder instance, allowing for method chaining
     */
    public MethodImplementationStrategyBuilder<T> setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    /**
     * Registers an {@code InvocationHandler} for the specified method name and parameter types.
     *
     * @param methodName the name of the method to register the handler for
     * @param args       the parameter types of the method
     * @return a {@code BuildOptions.Set} for configuring the {@code InvocationHandler} for the method
     * @throws ReflectionException if the method cannot be found
     */
    public BuildOptions.Set<MethodImplementationStrategyBuilder<T>, InterceptableInvocationHandler> forMethod(
            String methodName, Class<?>... args) throws ReflectionException {
        Method m = Methods.lookup(interfaceType, methodName, args);
        return invocationHandler ->
                (MethodImplementationStrategyBuilder<T>) forKey(m).set(invocationHandler);
    }

    private List<Method> getMethodsWithoutHandlers(Class<T> interfaceType) {
        return Arrays.stream(interfaceType.getMethods())
                .filter(method -> Objects.isNull(getInstance().getHandler(method)))
                .collect(Collectors.toList());
    }

    /**
     * Configures the methods of the interface type to use the fallback instance when no registered handler is found.
     * Depending on the strict mode setting, the builder will either require an exact method signature match between
     * the interface and fallback instance or will proceed without enforcing an exact match.
     *
     * @param fallbackInstance the fallback instance to use
     */
    private void setupFallbackHandlers(Object fallbackInstance) {
        DsExceptions.argue(Objects.nonNull(fallbackInstance));

        for (Method method : getMethodsWithoutHandlers(interfaceType)) {
            Optional.ofNullable(Methods.findMatching(fallbackInstance.getClass(), method))
                    .map(matchingMethod -> new RedirectInvocationHandlerBuilder<>()
                            .forMethod(matchingMethod)
                            .build())
                    .ifPresent(matchingMethodHandler -> getInstance().registerHandler(method, matchingMethodHandler));
        }
    }

    /**
     * must be called after setupFallbackHandlers
     */
    private void enforceStrictMode() {
        List<Method> methodsWithoutHandlers = getMethodsWithoutHandlers(interfaceType);

        if (methodsWithoutHandlers.isEmpty()) {
            return;
        }

        String listMethods = methodsWithoutHandlers.stream()
                .map(Method::toString)
                .collect(Collectors.joining("\n"));
        throw new ProxyConfigurationException(
                String.format("Strict mode: Cannot build instance if all methods of the interface `%s` " +
                                "do not have an appropriate handler. Methods without handlers:\n%s",
                        interfaceType, listMethods));
    }

    /**
     * Constructs an {@code InvocationStrategy} instance based on the current configuration of the builder.
     * This method configures the methods of the interface type to use the fallback instance when no registered
     * handler is found, taking the strict mode setting into consideration.
     *
     * @return a new {@code InvocationStrategy} instance with the configured settings
     */
    @Override
    public InvocationStrategy build() {
        fallbackInstances.forEach(this::setupFallbackHandlers);
        if (strictMode) enforceStrictMode();
        return super.build();
    }


}
