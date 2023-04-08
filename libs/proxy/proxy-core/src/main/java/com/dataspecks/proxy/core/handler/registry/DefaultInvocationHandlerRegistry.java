package com.dataspecks.proxy.core.handler.registry;

import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.builder.option.OptionIntercept;
import com.dataspecks.proxy.core.builder.option.OptionSet;
import com.dataspecks.proxy.core.handler.base.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;
import com.dataspecks.proxy.utils.exception.DsExceptions;
import com.dataspecks.proxy.utils.handler.base.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class DefaultInvocationHandlerRegistry extends DefaultRegistry<InvocationHandler, Method>
        implements InvocationHandlerRegistry {

    private InstanceRegistry instanceRegistry;

    @Override
    protected InvocationHandler computeValue(Method key, InvocationHandler currentHandler) {
        if (currentHandler == null) {
            return buildInvocationHandler(key);
        }
        if (currentHandler instanceof DelegatingInvocationHandler delegatingHandler) {
            if (delegatingHandler.isTargetHandlerUninitialized()) {
                delegatingHandler.initialize(buildInvocationHandler(key));
            }
        }
        return currentHandler;
    }

    private InvocationHandler buildInvocationHandler(Method key) {
        return Optional.ofNullable(instanceRegistry)
                .map(o -> o.find(key))
                .map(inst -> InvocationHandlers.fromMethodCall(inst, Methods.findMatching(inst.getClass(), key)))
                .orElse(InvocationHandlers.DEAD_END);
    }

    @Override
    public Method resolveKey(Object proxy, Method method, Object... args) {
        return method;
    }

    public static class Builder {
        private final DefaultInvocationHandlerRegistry registry = new DefaultInvocationHandlerRegistry();

        public Builder setFallbackRegistry(InstanceRegistry instanceRegistry) {
            registry.instanceRegistry = instanceRegistry;
            return this;
        }

        public ForMethodOptions forMethod(Method m) {
            return new ForMethodOptions(this, m);
        }

        public DefaultInvocationHandlerRegistry build() {
            DsExceptions.ensure(Objects.nonNull(this.registry.instanceRegistry));
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
                DsExceptions.ensure(Objects.isNull(that.registry.registered(m)), "Invocation handler already set");
                that.registry.register(m, val);
                return that;
            }

            @Override
            public Builder intercept(InvocationInterceptor interceptor) {
                DelegatingInvocationHandler invocationHandler = DelegatingInvocationHandler.builder()
                        .setInterceptor(interceptor)
                        .build();
                Optional.ofNullable(that.registry.registered(m))
                        .ifPresent(invocationHandler::initialize);
                that.registry.unregister(m);
                that.registry.register(m, invocationHandler);
                return that;
            }
        }
    }

    public static DefaultInvocationHandlerRegistry.Builder builder() {
        return new DefaultInvocationHandlerRegistry.Builder();
    }
}
