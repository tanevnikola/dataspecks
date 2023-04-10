package com.dataspecks.test;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.DynamicProxy;
import com.dataspecks.proxy.core.handler.base.InvocationInterceptor;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;


public class Playground {
    @Test
    public void dynamicProxyBuilderTest() throws ReflectionException {
        InvocationInterceptor traceInfo = ctx -> {
            String msg = String.format("Args: '%s', Intercepted '%s'",
                    Arrays.toString(ctx.args()), ctx.method());
            System.out.println(msg);
            return ctx.proceed();
        };
        IncompleteDummyClassA a = new IncompleteDummyClassA();
        IncompleteDummyClassB b = new IncompleteDummyClassB();
        DummyInterface dummy = DynamicProxy.builder(DummyInterface.class)
                .setFallbackInstances(a, b)

                .forMethod("getA").intercept(ctx -> {
                    Method m = Methods.lookup(a.getClass(), "unknown", Integer.class);
                    return ctx.proceed(m, 5);
                })
                .forMethod("getA").intercept(traceInfo)

                .forMethod("foo", Integer.class).intercept(traceInfo)
                .forMethod(Methods.lookup(Object.class, "toString")).intercept(traceInfo)
                .forMethod(Methods.lookup(Object.class, "hashCode")).intercept(traceInfo)

                .build();

        System.out.println(dummy.getA());
        System.out.println(dummy.foo(12));
    }

}
