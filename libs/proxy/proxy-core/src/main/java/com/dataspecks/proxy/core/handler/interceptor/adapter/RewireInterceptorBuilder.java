package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.builder.contract.handler.interceptor.adapter.RewireInterceptorBuildContract;

/**
 * Concrete builder
 */
public final class RewireInterceptorBuilder extends GenericBuilder<RewireInterceptor>
        implements Builder<RewireInterceptor>,
        RewireInterceptorBuildContract<RewireInterceptorBuilder> {

    public RewireInterceptorBuilder() {
        super(RewireInterceptor::new);
    }

    public BuildOptions.Set<RewireInterceptorBuilder, RewireOperation> forArgument(int index) {

        return rewire -> {
            configure(rInterceptor -> rInterceptor.setOperationForArgument(index, rewire));
            return this;
        };
    }
}
