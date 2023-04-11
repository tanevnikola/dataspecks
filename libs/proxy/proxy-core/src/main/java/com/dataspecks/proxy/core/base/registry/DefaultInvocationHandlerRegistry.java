package com.dataspecks.proxy.core.base.registry;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.builder.option.OptionIntercept;
import com.dataspecks.proxy.builder.option.OptionSetInvocationHandler;
import com.dataspecks.proxy.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.base.handler.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.extended.registry.InstanceRegistry;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public abstract class DefaultInvocationHandlerRegistry<K> extends DefaultRegistry<InvocationHandler, K>
        implements InvocationHandlerRegistry<K> {

    private InstanceRegistry<K> instanceRegistry;

    @Override
    protected InvocationHandler computeValue(K key, InvocationHandler currentHandler) {
        if (currentHandler == null) {
            return buildInvocationHandler();
        }
        if (currentHandler instanceof DelegatingInvocationHandler delegatingHandler
                && delegatingHandler.isTargetHandlerUninitialized()) {
            delegatingHandler.initialize(buildInvocationHandler());
        }

        return currentHandler;
    }

    /**
     * Returns an {@link InvocationHandler} that will lazily query the {@link InstanceRegistry} to find an
     * instance dedicated for this method call. Additionally, it will find the appropriate method from this instance
     * that matches the method in the proxy. If the instance or the method is not found, a {@link ProxyInvocationException}
     * will be thrown on invocation.
     * <p/>
     * It caches the matching instance and method for subsequent calls to the same method, improving performance.
     *
     * @return an {@link InvocationHandler} that can be used to invoke methods on instances retrieved from the registry
     */
    private InvocationHandler buildInvocationHandler() {
        return new InvocationHandler() {
            private Method methodToCall = null;
            private Object instance = null;
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Objects.isNull(methodToCall) || !methodToCall.equals(method)) {
                    if (Objects.isNull(instance = instanceRegistry.find(proxy, method, args))) {
                        throw new ProxyInvocationException(
                                String.format("Cannot find instance that matches method '%s'", method));
                    }
                    try {
                        methodToCall = Methods.getMatching(instance.getClass(), method);
                    } catch (ReflectionException e) {
                        throw new ProxyInvocationException(
                                String.format("Cannot find method that matches '%s' in '%s'",
                                        method, instance.getClass()), e);
                    }
                }
                return Methods.invoke(instance, methodToCall, args);
            }
        };
    }

    /**
     *
     */
    public static class Builder<B, K> implements
            OptionSetRegistry<B, InstanceRegistry<K>> {

        private B builder;
        private DefaultInvocationHandlerRegistry<K> registry;

        protected void initialize(B builder, DefaultInvocationHandlerRegistry<K> registry) {
            this.builder = builder;
            this.registry = registry;
        }

        public B setRegistry(InstanceRegistry<K> instanceRegistry) {
            Objects.requireNonNull(instanceRegistry);
            registry.instanceRegistry = instanceRegistry;
            return builder;
        }

        public ForMethodOptions<B, K> forMethod(K key) {
            return new ForMethodOptions<>(this, key);
        }

        public DefaultInvocationHandlerRegistry<K> build() {
            DsExceptions.precondition(Objects.nonNull(this.registry.instanceRegistry));
            return registry;
        }

        /**
         *
         */
        public record ForMethodOptions<B, K>(Builder<B, K> that, K key) implements
                OptionSetInvocationHandler<Builder<B, K>>,
                OptionIntercept<Builder<B, K>> {

            @Override
            public Builder<B, K> setInvocationHandler(InvocationHandler val) {
                if (that.registry.registered(key) instanceof DelegatingInvocationHandler delegating) {
                    if (delegating.isTargetHandlerUninitialized()) {
                        delegating.initialize(val);
                        return that;
                    }
                }
                DsExceptions.precondition(Objects.isNull(that.registry.registered(key)),
                        "Invocation handler already set");
                that.registry.register(key, val);
                return that;
            }

            @Override
            public Builder<B, K> intercept(InvocationInterceptor interceptor) {
                DelegatingInvocationHandler delegatingInvocationHandler = DelegatingInvocationHandler.builder()
                        .intercept(interceptor)
                        .build();

                Optional.ofNullable(that.registry.registered(key))
                        .filter(invocationHandler -> invocationHandler instanceof  DelegatingInvocationHandler)
                        .map(DelegatingInvocationHandler.class::cast)
                        .ifPresentOrElse(
                                existingDelegatingInvocationHandler ->
                                        existingDelegatingInvocationHandler.initialize(delegatingInvocationHandler),
                                () -> {
                                    that.registry.unregister(key);
                                    that.registry.register(key, delegatingInvocationHandler);
                                });
                return that;
            }
        }
    }
}