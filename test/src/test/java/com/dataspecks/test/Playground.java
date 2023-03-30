package com.dataspecks.test;

import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.proxy.core.ProxyBuilder;
import com.dataspecks.proxy.core.handler.base.strategy.StrategyBasedInvocationHandlerBuilder;
import com.dataspecks.proxy.core.handler.extended.method.MethodImplementationStrategyBuilder;
import org.junit.Test;


public class Playground {

    @Test
    public void newPlayground() throws ReflectionException {
        DummyClassInterface dummy = new ProxyBuilder<>(DummyClassInterface.class).withHandler(
                new StrategyBasedInvocationHandlerBuilder().fromStrategy(
                        new MethodImplementationStrategyBuilder<>(DummyClassInterface.class)
                                .setStrictMode(true)
                                .addFallbackInstance(new IncompleteDummyClass())
                                .addFallbackInstance(this)
                                .build()));

        System.out.println(dummy.foo(1));
        System.out.println(dummy.getA());
    }

    public String foo(Integer x) {
        return "Hello world " + x;
    }

    public Integer getA() {
        return 5;
    }

}
