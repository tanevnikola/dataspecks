package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;

public interface RewireInterceptorBuilder extends Builder<RewireInterceptor> {
    BuildOptions.Set<RewireInterceptorBuilder, RewireOperation> forArgument(int index);
}
