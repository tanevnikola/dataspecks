package com.dataspecks.test;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.DynamicProxy;
import com.dataspecks.proxy.core.ProxyBuilder;
import com.dataspecks.proxy.core.handler.base.DelegatingInvocationHandler;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;
import com.dataspecks.proxy.core.handler.base.ProxyInvocationHandler;
import com.dataspecks.proxy.core.handler.registry.DefaultInstanceRegistry;
import com.dataspecks.proxy.core.handler.registry.DefaultInvocationHandlerRegistry;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;


public class Playground {
    @Test
    public void incompleteHandlerInstance1() throws ReflectionException {

        ProxyInvocationHandler proxyInvocationHandler = ProxyInvocationHandler.builder()
                .setRegistry(DefaultInvocationHandlerRegistry.builder()
                        .setRegistry(DefaultInstanceRegistry.fromInstances(
                                new IncompleteDummyClassA(),
                                new IncompleteDummyClassB()))
                        .forMethod(Methods.lookup(DummyInterface.class, "getA")).intercept(ctx -> {
                            String msg = String.format("Intercepted '%s'", ctx.method());
                            System.out.println(msg);
                            return ctx.proceed();
                        })
                        .forMethod(Methods.lookup(Object.class, "toString")).intercept(ctx -> {
                            String msg = String.format("Intercepted '%s'", ctx.method());
                            System.out.println(msg);
                            return ctx.proceed();
                        })
                        .build())
                .build();

        DummyInterface dummy = new ProxyBuilder<>(DummyInterface.class).withHandler(proxyInvocationHandler);
        System.out.println(dummy.getA());
        System.out.println(dummy.getA());
        System.out.println(dummy.foo(1));
        System.out.println(dummy);
        System.out.println(dummy.hashCode());
    }

    @Test
    public void testDelegate() {
        InvocationHandler a = (proxy, method, args) -> null;
        DelegatingInvocationHandler handler = DelegatingInvocationHandler.builder().build();
        handler.initialize(a);
        handler.initialize(a);
    }


    @Test
    public void dynamicProxyBuilderTest() throws ReflectionException {
        InvocationInterceptor interceptor = ctx -> {
            Object res = ctx.proceed();
            String msg = String.format("Value '%s', Intercepted '%s'",res, ctx.method());
            System.out.println(msg);
            return res;
        };
        IncompleteDummyClassA a = new IncompleteDummyClassA();
        IncompleteDummyClassB b = new IncompleteDummyClassB();
        DummyInterface dummy = DynamicProxy.builder(DummyInterface.class)
                .setFallbackInstances(a, b)

                // .forMethod("getA").setFallbackInstance(new DummyClass())

                .forMethod("getA").intercept(interceptor)
                .forMethod("getA").intercept(interceptor)
                .forMethod("getA").intercept(interceptor)
                //.forMethod("getA").set((proxy, method, args) -> 11111)

                .forMethod("foo", Integer.class).intercept(interceptor)
                .forMethod("foo", Integer.class).intercept(interceptor)
                .forMethod("foo", Integer.class).intercept(interceptor)
                .forMethod("foo", Integer.class).intercept(interceptor)
                .forMethod("foo", Integer.class).intercept(interceptor)

                .forMethod(Methods.lookup(Object.class, "toString")).intercept(interceptor)
                .forMethod(Methods.lookup(Object.class, "hashCode")).intercept(interceptor)

                .build();

        dummy.getA();
        dummy.foo(12);
    }

}
