package com.dataspecks.proxy.core.base.registry;

import com.dataspecks.proxy.builder.option.OptionAddInvocationHandlerForKey;
import com.dataspecks.proxy.builder.option.OptionInterceptForKey;
import com.dataspecks.proxy.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.base.handler.ConcreteInvocationHandler;
import com.dataspecks.proxy.core.base.handler.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

public abstract class InvocationHandlerRegistry<K> extends DefaultRegistry<InvocationHandler, K> {

    private InstanceRegistry<K> instanceRegistry;

    @Override
    protected InvocationHandler computeValue(K key, InvocationHandler currentHandler) {
        if (currentHandler == null) {
            return new ConcreteInvocationHandler<>();
        }
        if (currentHandler instanceof DelegatingInvocationHandler) {
            @SuppressWarnings("unchecked")
            DelegatingInvocationHandler<K> delegatingHandler = (DelegatingInvocationHandler<K>) currentHandler;
            if (delegatingHandler.isTargetHandlerUninitialized()) {
                delegatingHandler.initialize(new ConcreteInvocationHandler<>());
            }
        }
        return currentHandler;
    }

    public InstanceRegistry<K> getInstanceRegistry() {
        return instanceRegistry;
    }

    /**
     *
     */
    public static final class Builder<K> implements
            OptionSetRegistry<Builder<K>, InstanceRegistry<K>>,
            OptionAddInvocationHandlerForKey<Builder<K>, K>,
            OptionInterceptForKey<Builder<K>, K> {

        private final InvocationHandlerRegistry<K> registry;

        public Builder(InvocationHandlerRegistry<K> registry) {
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
            InvocationHandler registeredInvocationHandler = registry.registered(key);
            if (registeredInvocationHandler instanceof DelegatingInvocationHandler) {
                @SuppressWarnings("unchecked")
                DelegatingInvocationHandler<K> delegating = (DelegatingInvocationHandler<K>) registeredInvocationHandler;
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
        public Builder<K> intercept(K key, InvocationInterceptor<K> interceptor) {
            DelegatingInvocationHandler<K> delegatingInvocationHandler = DelegatingInvocationHandler.<K>builder()
                    .intercept(interceptor)
                    .build();
            InvocationHandler currentInvocationHandler = registry.registered(key);
            if (currentInvocationHandler instanceof DelegatingInvocationHandler) {
                @SuppressWarnings("unchecked")
                DelegatingInvocationHandler<K> delegate = (DelegatingInvocationHandler<K>) currentInvocationHandler;
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

        public InvocationHandlerRegistry<K> build() {
            DsExceptions.precondition(Objects.nonNull(this.registry.instanceRegistry));
            return registry;
        }
    }

}