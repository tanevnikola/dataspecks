package com.dataspecks.proxy.utils.handler.base.interceptor;

import com.dataspecks.proxy.core.handler.base.interceptor.ArgumentsInterceptor;

public final class ArgumentsInterceptors {
    private ArgumentsInterceptors() {}


    /**
     * A pass-through arguments interceptor (without modification of the original arguments)
     */
    public static final ArgumentsInterceptor PassThrough = (proxy, method, args) -> args;
}
