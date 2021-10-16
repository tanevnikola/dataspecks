package com.dataspecks.proxy.core.handler.composite;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.core.builder.contract.handler.RedirectInvocationHandlerBuildContract;
import com.dataspecks.proxy.core.builder.contract.handler.dynamic.DynamicInvocationHandlerBuildContract;
import com.dataspecks.proxy.core.handler.dynamic.InvocationStrategy;

import java.lang.reflect.Method;

public class ProxyInvocationHandlerBuilder<T> implements
        Builder<ProxyInvocationHandler<T>>,
        DynamicInvocationHandlerBuildContract<T, ProxyInvocationHandlerBuilder<T>>,
        RedirectInvocationHandlerBuildContract<T, ProxyInvocationHandlerBuilder<T>> {
    @Override
    public ProxyInvocationHandler<T> build() {
        return null;
    }

    @Override
    public ProxyInvocationHandlerBuilder<T> setMethod(String name, Class<?>... args) {
        return null;
    }

    @Override
    public ProxyInvocationHandlerBuilder<T> setMethod(Method method) {
        return null;
    }

    @Override
    public ProxyInvocationHandlerBuilder<T> setStrategy(Builder<? extends InvocationStrategy<T>> iSBuilder) {
        return null;
    }
}
