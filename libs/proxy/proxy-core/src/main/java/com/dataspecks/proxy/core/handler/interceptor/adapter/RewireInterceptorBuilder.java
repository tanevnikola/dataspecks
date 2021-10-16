package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.Builder;

public interface RewireInterceptorBuilder extends
        Builder<RewireInterceptor>,
        RewireInterceptorBuildContract<RewireInterceptorBuilder> {

    static RewireInterceptorBuilder create() {
        return new RewireInterceptor.BuilderImpl();
    }
}
