package com.dataspecks.proxy.core.handler.base.strategy;

import com.dataspecks.commons.exception.unchecked.ArgueException;
import com.dataspecks.proxy.utils.handler.base.strategy.InvocationStrategies;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.InvocationHandler;

public class StrategyBasedInvocationHandlerBuilderTest {

    @Test
    public void testBuild_withDefaultStrategy() {
        InvocationHandler handler = new StrategyBasedInvocationHandlerBuilder()
                .build();

        Assertions.assertNotNull(handler);
    }

    @Test
    public void testBuild_withCustomStrategy() {
        InvocationHandler handler = new StrategyBasedInvocationHandlerBuilder()
                .fromStrategy(InvocationStrategies.NO_OP);

        Assertions.assertNotNull(handler);
        Assertions.assertEquals(InvocationStrategies.NO_OP, ((StrategyBasedInvocationHandler) handler).getStrategy());
    }

    @Test(expected = ArgueException.class)
    public void testBuild_withNullStrategy() {
        new StrategyBasedInvocationHandlerBuilder()
                .fromStrategy(null);
    }

}
