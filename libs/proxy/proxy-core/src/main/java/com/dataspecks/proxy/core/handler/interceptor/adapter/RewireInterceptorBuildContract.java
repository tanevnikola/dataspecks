package com.dataspecks.proxy.core.handler.interceptor.adapter;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;

public interface RewireInterceptorBuildContract<B extends Builder<? extends ArgumentsInterceptor>>{
    BuildOptions.Set<B, RewireOperation> forArgument(int index);
}
