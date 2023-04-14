package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionIntercept;

import java.lang.reflect.Method;

/**
 *
 */
public abstract class InterceptableInvocationHandler<K> extends SuperInvocationHandler<K> {
    private InvocationInterceptor<K> interceptor = null;

    protected InterceptableInvocationHandler() {}

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return interceptor != null
                ? interceptor.intercept(new InvocationContext<>(this, proxy, method, args))
                : proceed(proxy, method, args);
    }

    protected abstract Object proceed(Object proxy, Method method, Object[] args) throws Throwable;

    /**
     *
     */
    protected static final class Builder<K> implements
            OptionIntercept<Builder<K>, K> {

        private final InterceptableInvocationHandler<K> interceptableInvocationHandler;

        protected Builder(InterceptableInvocationHandler<K> interceptableInvocationHandler) {
            this.interceptableInvocationHandler = interceptableInvocationHandler;
        }

        @Override
        public Builder<K> intercept(InvocationInterceptor<K> interceptor) {
            interceptableInvocationHandler.interceptor = interceptor;
            return this;
        }
    }
}
