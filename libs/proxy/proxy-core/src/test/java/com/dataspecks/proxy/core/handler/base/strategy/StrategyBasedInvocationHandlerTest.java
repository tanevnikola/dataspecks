package com.dataspecks.proxy.core.handler.base.strategy;

import com.dataspecks.proxy.core.handler.base.strategy.InvocationStrategy;
import com.dataspecks.proxy.core.handler.base.strategy.StrategyBasedInvocationHandler;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StrategyBasedInvocationHandlerTest {

    private StrategyBasedInvocationHandler handler;
    private InvocationStrategy strategy;
    private InvocationHandler invocationHandler;
    private Object proxy;
    private Method method;
    private Object[] args;

    @Before
    public void setUp() throws NoSuchMethodException {
        strategy = mock(InvocationStrategy.class);
        invocationHandler = mock(InvocationHandler.class);
        handler = new StrategyBasedInvocationHandler();
        proxy = new Object();
        method = Object.class.getMethod("hashCode"); // since we cannot mock final
        args = new Object[] { "arg1", "arg2" };
    }

    @Test
    public void testGetSetStrategy() {
        handler.setStrategy(strategy);
        assertEquals(strategy, handler.getStrategy());
    }

    @Test
    public void testInvoke() throws Throwable {
        when(strategy.selectHandler(proxy, method, args)).thenReturn(invocationHandler);
        handler.setStrategy(strategy);
        handler.invoke(proxy, method, args);
        verify(invocationHandler).invoke(proxy, method, args);
    }

    @Test(expected = Throwable.class)
    public void testInvokeThrowsException() throws Throwable {
        when(strategy.selectHandler(proxy, method, args)).thenThrow(new Throwable());
        handler.setStrategy(strategy);
        handler.invoke(proxy, method, args);
    }
}
