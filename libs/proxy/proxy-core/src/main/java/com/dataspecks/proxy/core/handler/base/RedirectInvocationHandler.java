package com.dataspecks.proxy.core.handler.base;

import java.lang.reflect.Method;

public final class RedirectInvocationHandler<U> extends MethodInvocationHandler<U> {

    private Method targetMethod = null;

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object... args) throws Throwable {
        return super.doInvoke(proxy, getTargetMethod(), args);
    }
}
