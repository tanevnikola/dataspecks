package com.dataspecks.proxy.core.handler.registry;

import com.dataspecks.proxy.core.builder.option.OptionIntercept;
import com.dataspecks.proxy.core.builder.option.OptionSet;
import com.dataspecks.proxy.core.builder.option.OptionSetRegistry;
import com.dataspecks.proxy.core.handler.base.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;
import com.dataspecks.proxy.utils.exception.DsExceptions;
import com.dataspecks.proxy.utils.handler.base.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class DefaultInvocationHandlerRegistry extends DefaultRegistry<InvocationHandler, Method>
        implements InvocationHandlerRegistry<Method> {

    private InstanceRegistry<Method> instanceRegistry;

    @Override
    protected InvocationHandler computeValue(Method key, InvocationHandler currentHandler) {
        if (currentHandler == null) {
            return InvocationHandlers.fromInstanceRegistry(instanceRegistry);
        }
        if (currentHandler instanceof DelegatingInvocationHandler delegatingHandler
                && delegatingHandler.isTargetHandlerUninitialized()) {
            delegatingHandler.initialize(InvocationHandlers.fromInstanceRegistry(instanceRegistry));
        }

        return currentHandler;
    }

    @Override
    public Method resolveKey(Object proxy, Method method, Object... args) {
        return method;
    }

    public static class Builder implements
            OptionSetRegistry<Builder, InstanceRegistry<Method>> {

        private final DefaultInvocationHandlerRegistry registry = new DefaultInvocationHandlerRegistry();

        public Builder setRegistry(InstanceRegistry<Method> instanceRegistry) {
            registry.instanceRegistry = instanceRegistry;
            return this;
        }

        public ForMethodOptions forMethod(Method m) {
            return new ForMethodOptions(this, m);
        }

        public DefaultInvocationHandlerRegistry build() {
            DsExceptions.precondition(Objects.nonNull(this.registry.instanceRegistry));
            return registry;
        }

        /**
         *
         */
        public record ForMethodOptions(Builder that, Method m) implements
                OptionSet<Builder, InvocationHandler>,
                OptionIntercept<Builder> {

            @Override
            public Builder set(InvocationHandler val) {
                if (that.registry.registered(m) instanceof DelegatingInvocationHandler delegating) {
                    if (delegating.isTargetHandlerUninitialized()) {
                        delegating.initialize(val);
                        return that;
                    }
                }
                DsExceptions.precondition(Objects.isNull(that.registry.registered(m)),
                        "Invocation handler already set");
                that.registry.register(m, val);
                return that;
            }

            @Override
            public Builder intercept(InvocationInterceptor interceptor) {
                DelegatingInvocationHandler delegatingInvocationHandler = DelegatingInvocationHandler.builder()
                        .intercept(interceptor)
                        .build();

                Optional.ofNullable(that.registry.registered(m))
                        .filter(invocationHandler -> invocationHandler instanceof  DelegatingInvocationHandler)
                        .map(DelegatingInvocationHandler.class::cast)
                        .ifPresentOrElse(
                                existingDelegatingInvocationHandler ->
                                        existingDelegatingInvocationHandler.initialize(delegatingInvocationHandler),
                                () -> {
                                    that.registry.unregister(m);
                                    that.registry.register(m, delegatingInvocationHandler);
                                });
                return that;
            }
        }
    }

    public static DefaultInvocationHandlerRegistry.Builder builder() {
        return new DefaultInvocationHandlerRegistry.Builder();
    }
}
