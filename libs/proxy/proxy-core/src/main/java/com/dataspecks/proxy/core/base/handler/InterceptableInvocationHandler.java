package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionIntercept;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 */
public abstract class InterceptableInvocationHandler implements InvocationHandler {
    private InvocationInterceptor interceptor = null;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return interceptor != null
                ? interceptor.intercept(new InvocationContext(this, proxy, method, args))
                : proceed(proxy, method, args);
    }

    protected abstract Object proceed(Object proxy, Method method, Object[] args) throws Throwable;

    /**
     *
     * @param <B>
     */
    protected static class Builder<B extends Builder<?>> implements
            OptionIntercept<B> {

        private final InterceptableInvocationHandler interceptableInvocationHandler;

        protected Builder(InterceptableInvocationHandler interceptableInvocationHandler) {
            this.interceptableInvocationHandler = interceptableInvocationHandler;
        }

        @Override
        public B intercept(InvocationInterceptor interceptor) {
            interceptableInvocationHandler.interceptor = interceptor;
            @SuppressWarnings("unchecked")
            B thiz = (B)this;
            return thiz;
        }

       public InterceptableInvocationHandler build() {
            return interceptableInvocationHandler;
        }


    }
}
