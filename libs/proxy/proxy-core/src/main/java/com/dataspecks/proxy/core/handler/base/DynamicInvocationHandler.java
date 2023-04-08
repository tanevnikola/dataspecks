package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.proxy.utils.handler.base.Interceptors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class DynamicInvocationHandler implements InvocationHandler {
    private InvocationInterceptor interceptor = Interceptors.NO_OP;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return interceptor.intercept(new InvocationContext(this, proxy, method, args));
    }

    protected abstract Object proceed(Object proxy, Method method, Object[] args) throws Throwable;

    /**
     *
     * @param <TExtendedBuilder>
     */
    protected static class Builder<TExtendedBuilder extends Builder<?>> {

        private final DynamicInvocationHandler dynamicInvocationHandler;

        protected Builder(DynamicInvocationHandler dynamicInvocationHandler) {
            this.dynamicInvocationHandler = dynamicInvocationHandler;
        }

        public TExtendedBuilder setInterceptor(InvocationInterceptor interceptor) {
            dynamicInvocationHandler.interceptor = interceptor;
            @SuppressWarnings("unchecked")
            TExtendedBuilder thiz = (TExtendedBuilder)this;
            return thiz;
        }

        DynamicInvocationHandler build() {
            return dynamicInvocationHandler;
        }
    }
}
