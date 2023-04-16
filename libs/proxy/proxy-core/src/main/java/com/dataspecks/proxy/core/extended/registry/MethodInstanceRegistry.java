package com.dataspecks.proxy.core.extended.registry;

import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.builder.option.OptionSetFallbackInstance;
import com.dataspecks.proxy.builder.option.OptionSetFallbackInstances;
import com.dataspecks.proxy.core.base.registry.InstanceRegistry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodInstanceRegistry extends InstanceRegistry<Method> {

    private final List<Object> fallbackInstances = new ArrayList<>();

    @Override
    public Method resolveKey(Object proxy, Method method, Object[] args) {
        return method;
    }

    @Override
    protected Object computeValue(Method key, Object currentInstance) {
        return currentInstance != null
                ? currentInstance
                : Methods.findMatchingInstance(fallbackInstances, key);
    }

    /**
     *
     */
    public static final class Builder implements
            OptionSetFallbackInstances<Builder> {
        private final MethodInstanceRegistry registry = new MethodInstanceRegistry();

        @Override
        public Builder addFallbackInstances(Object... instances) {
            registry.fallbackInstances.addAll(Arrays.stream(instances).toList());
            return this;
        }

        public Builder.ForMethodOptions forMethod(Method m) {
            return new ForMethodOptions(this, m);
        }

        public MethodInstanceRegistry build() {
            return registry;
        }

        /**
         *
         */
        public record ForMethodOptions(Builder that, Method m) implements
                OptionSetFallbackInstance<Builder> {

            @Override
            public Builder setFallbackInstance(Object instance) {
                that.registry.register(m, instance);
                return that;
            }
        }
    }

    public static MethodInstanceRegistry.Builder builder() {
        return new MethodInstanceRegistry.Builder();
    }
}
