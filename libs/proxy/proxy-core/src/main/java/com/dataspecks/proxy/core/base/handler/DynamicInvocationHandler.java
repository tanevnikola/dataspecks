package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionIntercept;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class DynamicInvocationHandler implements InvocationHandler {
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

        private final DynamicInvocationHandler dynamicInvocationHandler;

        protected Builder(DynamicInvocationHandler dynamicInvocationHandler) {
            this.dynamicInvocationHandler = dynamicInvocationHandler;
        }

        @Override
        public B intercept(InvocationInterceptor interceptor) {
            dynamicInvocationHandler.interceptor = interceptor;
            @SuppressWarnings("unchecked")
            B thiz = (B)this;
            return thiz;
        }

       public  DynamicInvocationHandler build() {
            return dynamicInvocationHandler;
        }


    }
}
