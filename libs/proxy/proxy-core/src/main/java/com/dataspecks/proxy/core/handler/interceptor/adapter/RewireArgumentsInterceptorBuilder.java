package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.builder.contract.handler.interceptor.adapter.RewireArgumentsInterceptorBuildContract;
import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;

/**
 * Concrete builder
 */
public final class RewireArgumentsInterceptorBuilder extends GenericBuilder<ArgumentsInterceptor, RewireArgumentsInterceptor>
        implements RewireArgumentsInterceptorBuildContract<RewireArgumentsInterceptorBuilder> {

    public RewireArgumentsInterceptorBuilder() {
        super(RewireArgumentsInterceptor::new);
    }

    public BuildOptions.Set<RewireArgumentsInterceptorBuilder, RewireOperation> forArgument(int index) {

        return rewire -> {
            configure(rInterceptor -> rInterceptor.setOperationForArgument(index, rewire));
            return this;
        };
    }
}
