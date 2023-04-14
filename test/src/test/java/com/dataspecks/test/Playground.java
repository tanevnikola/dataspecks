package com.dataspecks.test;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.base.interceptor.InvocationAdapter;
import com.dataspecks.proxy.core.extended.handler.DynamicProxy;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;


public class Playground {

    private InvocationInterceptor getTraceInfo(Integer id) {
        Objects.requireNonNull(id);
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
        DummyInterface dummy = DynamicProxy.builder(DummyInterface.class)
                .addFallbackInstances(new DummyClass())
                .addFallbackInstances(new IncompleteDummyClassA())
                .addFallbackInstances(new IncompleteDummyClassB())
                .forMethod("getA").intercept(InvocationAdapter.<Method>builder()
                        .toMethod(Methods.lookup(IncompleteDummyClassB.class, "getB"))
                        .passResult()
                        .build())
                .forMethod("getB").intercept(InvocationAdapter.<Method>builder()
                        .toMethod(Methods.lookup(IncompleteDummyClassA.class, "getA"))
                        .passResult()
                        .build())
                .build();
        System.out.println(dummy.getA());
        System.out.println(dummy.getB());
    }
}
