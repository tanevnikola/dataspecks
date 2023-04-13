package com.dataspecks.test;

import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.core.base.interceptor.InvocationAdapter;
import com.dataspecks.proxy.core.extended.handler.DynamicProxy;
import org.junit.Test;

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
    public void testAdapter() {
        DummyInterface dummy = DynamicProxy.builder(DummyInterface.class)
                .addFallbackInstances(new IncompleteDummyClassA())
                .addFallbackInstances(new IncompleteDummyClassB())
                .forMethod("foo", Integer.class).intercept(InvocationAdapter.builder()
                        .setResultAdapter(arg -> arg)
                        .forArguments(0).setValueProducer(args -> 23)
                        .build())
                .build();
        System.out.println(dummy.foo(12));
    }
}
