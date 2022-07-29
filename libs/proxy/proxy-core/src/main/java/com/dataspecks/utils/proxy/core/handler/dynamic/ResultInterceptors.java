package com.dataspecks.utils.proxy.core.handler.dynamic;

import com.dataspecks.proxy.core.handler.interceptor.ResultInterceptor;

public final class ResultInterceptors {
    private ResultInterceptors() {}


    /**
     * A pass-through result interceptor (without modification of the result)
     */
    public static final ResultInterceptor<Object> PassThrough = (proxy, method, arg) -> arg;
}
