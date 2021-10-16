package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.builder.contract.handler.interceptor.adapter.RewireInterceptorBuildContract;
import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;

/**
 * Concrete builder
 */
public final class RewireInterceptorBuilder extends GenericBuilder<ArgumentsInterceptor, RewireInterceptor>
        implements RewireInterceptorBuildContract<RewireInterceptorBuilder> {

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
