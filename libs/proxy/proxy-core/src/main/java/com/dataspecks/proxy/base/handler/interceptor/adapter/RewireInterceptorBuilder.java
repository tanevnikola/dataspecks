package com.dataspecks.proxy.base.handler.interceptor.adapter;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.base.builder.BuildOptions;

public interface RewireInterceptorBuilder extends Builder<RewireInterceptor> {
    BuildOptions.Set<RewireInterceptorBuilder, RewireOperation> forArgument(int index);
}
