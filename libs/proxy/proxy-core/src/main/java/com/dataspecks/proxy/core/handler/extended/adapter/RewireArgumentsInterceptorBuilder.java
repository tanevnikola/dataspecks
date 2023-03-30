package com.dataspecks.proxy.core.handler.extended.adapter;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.base.interceptor.ArgumentsInterceptor;

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
            instanceTemplate.getRewireMap().put(index, rewireOperation);
            return this;
        };
    }

    @Override
    public ArgumentsInterceptor build() {
        return null;
    }


}
