package com.dataspecks.utils.proxy.core.handler.dynamic;

import com.dataspecks.proxy.core.handler.interceptor.ArgumentsInterceptor;

public final class ArgumentsInterceptors {
    private ArgumentsInterceptors() {}


    /**
     * A pass-through arguments interceptor (without modification of the original arguments)
     */
    public static final ArgumentsInterceptor PassThrough = (proxy, method, args) -> args;
}
