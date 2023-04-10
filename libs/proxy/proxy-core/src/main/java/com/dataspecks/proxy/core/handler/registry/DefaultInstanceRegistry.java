package com.dataspecks.proxy.core.handler.registry;

import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.builder.option.OptionSetFallbackInstance;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DefaultInstanceRegistry extends DefaultRegistry<Object, Method>
        implements InstanceRegistry<Method> {

    private final List<Object> defaultInstances = new ArrayList<>();

    @Override
    public Method resolveKey(Object proxy, Method method, Object... args) {
        return method;
    }

    @Override
    protected Object computeValue(Method key, Object currentInstance) {
        return currentInstance != null ? currentInstance : findInstance(key);
    }

    private Object findInstance(Method key) {
        Object instanceCandidate = null;
        for (Object instance : defaultInstances) {
            Method methodCandidate = Methods.findMatching(instance.getClass(), key);
            if (methodCandidate != null) {
                if (Objects.isNull(instanceCandidate)) {
                    instanceCandidate = instance;
                }
                if (methodCandidate.equals(key)) {
                    return instance;
                }
            }
        }
        return instanceCandidate;
    }

    /**
     *
     */
    public static class Builder {
        private final DefaultInstanceRegistry registry = new DefaultInstanceRegistry();

        public Builder setFallbackInstances(Object... instances) {
            registry.defaultInstances.addAll(Arrays.stream(instances).toList());
            return this;
        }

        public Builder.ForMethodOptions forMethod(Method m) {
            return new ForMethodOptions(this, m);
        }

        public DefaultInstanceRegistry build() {
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

    public static DefaultInstanceRegistry.Builder builder() {
        return new DefaultInstanceRegistry.Builder();
    }

    public static DefaultInstanceRegistry fromInstances(Object... instances) {
        return DefaultInstanceRegistry.builder()
                .setFallbackInstances(instances)
                .build();
    }

}
