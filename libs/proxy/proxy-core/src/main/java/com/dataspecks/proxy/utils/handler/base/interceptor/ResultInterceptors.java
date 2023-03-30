package com.dataspecks.proxy.utils.handler.base.interceptor;

import com.dataspecks.proxy.core.handler.base.interceptor.ResultInterceptor;

public final class ResultInterceptors {
    private ResultInterceptors() {}


    /**
     * A pass-through result interceptor (without modification of the result)
     */
    public static final ResultInterceptor PassThrough = (proxy, method, result, args) -> result;
}
