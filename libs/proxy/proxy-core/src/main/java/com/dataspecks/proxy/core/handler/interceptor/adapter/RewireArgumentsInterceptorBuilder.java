package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete builder
 */
public final class RewireArgumentsInterceptorBuilder implements Builder<ArgumentsInterceptor> {
    private final RewireArgumentsInterceptor instanceTemplate = new RewireArgumentsInterceptor();

    public BuildOptions.Set<RewireArgumentsInterceptorBuilder, RewireOperation> forArgument(int index) {
        return rewireOperation -> {
            instanceTemplate.setOperationForArgument(index, rewireOperation);
            return this;
        };
    }

    @Override
    public ArgumentsInterceptor build() {
        return null;
    }

    /**
     * A specialized {@link ArgumentsInterceptor} that uses argument indexes to find the {@link RewireOperation} for that
     * argument.
     */
    private final static class RewireArgumentsInterceptor implements ArgumentsInterceptor  {
        private final Map<Integer, RewireOperation> rewireMap = new HashMap<>();

        /**
         * Sets a {@link RewireOperation} to be applied for specific argument
         * @param index the index of the argument we want the operation to be applied to
         * @param rewireOperation the {@link RewireOperation} to be applied
         */
        public void setOperationForArgument(int index, RewireOperation rewireOperation) {
            rewireMap.put(index, rewireOperation);
        }

        @Override
        public Object[] intercept(Object proxy, Method method, Object... args) throws Throwable {
            List<Object> result = new ArrayList<>();
            for (Integer key : rewireMap.keySet()) {
                result.add(rewireMap.get(key).perform(args));
            }
            return result.toArray();
        }
    }
}
