package com.dataspecks.proxy.core.handler.registry;

import com.dataspecks.commons.utils.reflection.Methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultInstanceRegistry extends DefaultRegistry<Object, Method>
        implements InstanceRegistry {

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
        for (Object instance : defaultInstances) {
            if (Methods.findMatching(instance.getClass(), key) != null) {
                return instance;
            }
        }
        return null;
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

        public DefaultInstanceRegistry build() {
            return registry;
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
