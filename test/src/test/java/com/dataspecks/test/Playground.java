package com.dataspecks.test;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.extended.handler.DynamicProxy;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.base.interceptor.InvocationAdapter;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;


public class Playground {
    @Test
    public void dynamicProxyBuilderTest() throws ReflectionException {
        IncompleteDummyClassA a = new IncompleteDummyClassA();
        IncompleteDummyClassB b = new IncompleteDummyClassB();
        DummyInterface dummy = DynamicProxy.builder(DummyInterface.class)
                .setFallbackInstances(a, b)

//                .forMethod("getA").intercept(ctx -> {
//                    Method m = Methods.lookup(a.getClass(), "unknown", Integer.class);
//                    // adapt args
//                    // call
//                    // adapt result
//                    return ctx.proceed(m, 5);
//                })

                .forMethod("getA").setInvocationHandler((proxy, method, args) -> 456)
                .forMethod("getA").intercept(getTraceInfo("1"))
                .forMethod("getA").intercept(getTraceInfo("2"))
                .forMethod("getA").intercept(getTraceInfo("3"))
                .forMethod("getA").intercept(getTraceInfo("4"))

                .build();

        System.out.println(dummy.getA());
    }

    private InvocationInterceptor getTraceInfo(String id) {
        return ctx -> {
            String msg = String.format("[%s] Args: '%s', Intercepted '%s'",
                    id, Arrays.toString(ctx.args()), ctx.method());
            System.out.println(msg);
            Object result = ctx.proceed();
            msg = String.format("[%s] Result '%s'",
                    id, result);
            System.out.println(msg);
            return result;
        };

    }

    @Test
    public void testAdapter() throws ReflectionException {
        IncompleteDummyClassA a = new IncompleteDummyClassA();
        IncompleteDummyClassB b = new IncompleteDummyClassB();
        DummyInterface dummy = DynamicProxy.builder(DummyInterface.class)
                .setFallbackInstances(a, b)
                .forMethod("getA").intercept(
                        InvocationAdapter.builder()
                                .setResultAdapter(null)
                                .addArgumentComposer(null)
                                .addArgumentComposer(null)
                                .build())
                .build();
    }

}
