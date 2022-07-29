package com.dataspecks.test;

import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.proxy.core.ProxyBuilder;
import com.dataspecks.proxy.core.handler.RedirectInvocationHandlerBuilder;
import com.dataspecks.proxy.core.handler.interceptor.adapter.RewireArgumentsInterceptorBuilder;
import com.dataspecks.proxy.core.handler.interceptor.adapter.RewireOperation;
import org.junit.Test;


public class Playground {

    @Test
    public void newPlayground() throws ReflectionException {
        DummyClassInterface dummy = new ProxyBuilder<>(DummyClassInterface.class)
                .setHandler(new RedirectInvocationHandlerBuilder<>(new ImmutableDummyClass())
                        .setMethod("getA"))
                .build();
        System.out.println(dummy.getA());


        RewireOperation.withArguments(1);
        RewireOperation.withArguments();
        new RewireArgumentsInterceptorBuilder()
                .forArgument(1).set(RewireOperation.withArguments(1, 2).set(val -> null));
    }

}
