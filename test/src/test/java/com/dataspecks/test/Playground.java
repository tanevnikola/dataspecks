package com.dataspecks.test;

import com.dataspecks.commons.core.exception.ReflectionException;
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
                .setFallbackInstances(new IncompleteDummyClassA(), new IncompleteDummyClassB())
                .forMethod("foo", Integer.class).intercept(InvocationAdapter.builder()
                        .setResultAdapter(arg -> 1)
                        .forArguments(1, 2, 3).setValueComposer(args -> 1)
                        .forArguments(4, 5, 6).setValueComposer(args -> 2)
                        .build())
                .build();

        dummy.foo(1);

    }
}
