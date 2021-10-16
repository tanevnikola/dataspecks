package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A specialized {@link ArgumentsInterceptor} that uses argument indexes to find the {@link RewireOperation} for that
 * argument.
 */
final class RewireInterceptor implements ArgumentsInterceptor  {
    private final Map<Integer, RewireOperation> rewireMap = new HashMap<>();

    private RewireInterceptor() {}

    @Override
    public Object[] intercept(Object proxy, Method method, Object... args) throws Throwable {
        List<Object> result = new ArrayList<>();
        for (Integer key : rewireMap.keySet()) {
            result.add(rewireMap.get(key).perform(args));
        }
        return result.toArray();
    }

    /**
     * Concrete builder
     */
    public static final class BuilderImpl extends GenericBuilder<RewireInterceptor>
            implements RewireInterceptorBuilder {

        public BuilderImpl() {
            super(RewireInterceptor::new);
        }

        public BuildOptions.Set<RewireInterceptorBuilder, RewireOperation> forArgument(int index) {

            return rewire -> {
                configure(rInterceptor -> rInterceptor.rewireMap.put(index, rewire));
                return this;
            };
        }
    }
}
