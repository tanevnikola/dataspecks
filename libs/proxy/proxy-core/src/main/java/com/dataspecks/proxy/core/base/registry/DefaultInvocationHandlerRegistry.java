package com.dataspecks.proxy.core.base.registry;

import com.dataspecks.proxy.builder.option.*;
import com.dataspecks.proxy.core.base.handler.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.base.handler.ConcreteInvocationHandler;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

public abstract class DefaultInvocationHandlerRegistry<K> extends DefaultRegistry<InvocationHandler, K>
        implements InvocationHandlerRegistry<K> {

    private InstanceRegistry<K> instanceRegistry;

    @Override
    protected InvocationHandler computeValue(K key, InvocationHandler currentHandler) {
        if (currentHandler == null) {
            return ConcreteInvocationHandler.<K>builder()
                    .setRegistry(instanceRegistry)
                    .build();
        }
        if (currentHandler instanceof DelegatingInvocationHandler delegatingHandler
                && delegatingHandler.isTargetHandlerUninitialized()) {
            delegatingHandler.initialize(
                    ConcreteInvocationHandler.<K>builder()
                            .setRegistry(instanceRegistry)
                            .build());
        }
        return currentHandler;
    }

    /**
     *
     */
    public static final class Builder<K> implements
            OptionSetRegistry<Builder<K>, InstanceRegistry<K>>,
            OptionAddInvocationHandlerForKey<Builder<K>, K>,
            OptionInterceptForKey<Builder<K>, K>{

        private final DefaultInvocationHandlerRegistry<K> registry;

        public Builder(DefaultInvocationHandlerRegistry<K> registry) {
            this.registry = registry;
        }

        @Override
        public Builder<K> setRegistry(InstanceRegistry<K> instanceRegistry) {
            Objects.requireNonNull(instanceRegistry);
            registry.instanceRegistry = instanceRegistry;
            return this;
        }

        @Override
        public Builder<K> addInvocationHandler(K key, InvocationHandler invocationHandler) {
            if (registry.registered(key) instanceof DelegatingInvocationHandler delegating) {
                if (delegating.isTargetHandlerUninitialized()) {
                    delegating.initialize(invocationHandler);
                    return this;
                }
            }
            DsExceptions.precondition(Objects.isNull(registry.registered(key)),
                    "Invocation handler already set");
            registry.register(key, invocationHandler);
            return this;
        }

        @Override
        public Builder<K> intercept(K key, InvocationInterceptor interceptor) {
            DelegatingInvocationHandler delegatingInvocationHandler = DelegatingInvocationHandler.builder()
                    .intercept(interceptor)
                    .build();
            InvocationHandler currentInvocationHandler = registry.registered(key);
            if (currentInvocationHandler instanceof DelegatingInvocationHandler delegate) {
                delegate.initialize(delegatingInvocationHandler);
            } else {
                if (Objects.nonNull(currentInvocationHandler)) {
                    delegatingInvocationHandler.initialize(currentInvocationHandler);
                    registry.unregister(key);
                }
                registry.register(key, delegatingInvocationHandler);
            }
            return this;
        }

        public DefaultInvocationHandlerRegistry<K> build() {
            DsExceptions.precondition(Objects.nonNull(this.registry.instanceRegistry));
            return registry;
        }
    }

}