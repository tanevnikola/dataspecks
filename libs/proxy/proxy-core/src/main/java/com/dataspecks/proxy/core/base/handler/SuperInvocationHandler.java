package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.core.base.registry.InstanceRegistry;
import com.dataspecks.proxy.core.base.registry.InvocationHandlerRegistry;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class SuperInvocationHandler<K> implements InvocationHandler {
    private InvocationHandlerRegistry<K> getInvocationHandlerRegistry(Object proxy) {
        DsExceptions.precondition(proxy instanceof Proxy, "Invalid proxy. Method called from invalid context.");
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
        if (invocationHandler instanceof ProxyInvocationHandler) {
            @SuppressWarnings("unchecked")
            ProxyInvocationHandler<K> proxyInvocationHandler = (ProxyInvocationHandler<K>) invocationHandler;
            return proxyInvocationHandler.getInvocationHandlerRegistry();
        }
        throw new ProxyInvocationException("Invalid proxy. Method called from invalid context.");
    }

    protected InstanceRegistry<K> getInstanceRegistry(Object proxy) {
        return this.getInvocationHandlerRegistry(proxy).getInstanceRegistry();
    }
}