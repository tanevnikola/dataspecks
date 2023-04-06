package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.commons.utils.reflection.Methods;

import java.lang.reflect.Method;

public class MethodInvocationHandler<U> extends InterceptableInvocationHandler {
    private U targetInstance = null;

    public U getTargetInstance() {
        return targetInstance;
    }

    public void setTargetInstance(U targetInstance) {
        this.targetInstance = targetInstance;
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        return Methods.invoke(getTargetInstance(), method, args);
    }
}
