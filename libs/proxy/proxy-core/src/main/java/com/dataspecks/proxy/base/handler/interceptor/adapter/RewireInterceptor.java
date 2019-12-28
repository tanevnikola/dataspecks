package com.dataspecks.proxy.base.handler.interceptor.adapter;

import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.base.builder.BuildOptions;
import com.dataspecks.proxy.base.handler.interceptor.ArgumentsInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A specialized {@link ArgumentsInterceptor} that uses argument indexes to find the {@link RewireOperation} for that
 * argument.
 */
public final class RewireInterceptor implements ArgumentsInterceptor  {
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

    public static RewireInterceptorBuilder builder() {
        return new BuilderImpl();
    }

    /**
     * Concrete builder
     */
    private static final class BuilderImpl extends GenericBuilder<RewireInterceptor>
            implements RewireInterceptorBuilder {

        private BuilderImpl() {
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