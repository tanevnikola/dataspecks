package com.dataspecks.proxy.core.base.registry;

import com.dataspecks.proxy.builder.option.OptionIntercept;
import com.dataspecks.proxy.builder.option.OptionSetInvocationHandler;
import com.dataspecks.proxy.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.base.handler.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.extended.registry.InstanceRegistry;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.util.Objects;

public abstract class DefaultInvocationHandlerRegistry<K> extends DefaultRegistry<InvocationHandler, K>
        implements InvocationHandlerRegistry<K> {

    private InstanceRegistry<K> instanceRegistry;

    @Override
    protected InvocationHandler computeValue(K key, InvocationHandler currentHandler) {
        if (currentHandler == null) {
            return instanceRegistry.buildInvocationHandler();
        }
        if (currentHandler instanceof DelegatingInvocationHandler delegatingHandler
                && delegatingHandler.isTargetHandlerUninitialized()) {
            delegatingHandler.initialize(instanceRegistry.buildInvocationHandler());
        }
        return currentHandler;
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
                InvocationHandler currentInvocationHandler = that.registry.registered(key);
                if (currentInvocationHandler instanceof DelegatingInvocationHandler delegate) {
                    delegate.initialize(delegatingInvocationHandler);
                } else {
                    if (Objects.nonNull(currentInvocationHandler)) {
                        delegatingInvocationHandler.initialize(currentInvocationHandler);
                        that.registry.unregister(key);
                    }
                    that.registry.register(key, delegatingInvocationHandler);
                }
                return that;
            }
        }
    }
}