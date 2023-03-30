package com.dataspecks.proxy.core.handler.base.simple;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

public class RedirectInvocationHandlerTest {
    private TestClass targetInstance;

    @Before
    public void setUp() {
        targetInstance = new TestClass();
    }

    @Test
    public void testInvoke_withValidTargetInstanceAndMethod() throws Exception {
        InvocationHandler handler = new RedirectInvocationHandlerBuilder<>(targetInstance)
                .fromMethod("testMethod", String.class);

        TestInterface proxy = (TestInterface) Proxy.newProxyInstance(
                TestInterface.class.getClassLoader(),
                new Class[]{TestInterface.class},
                handler
        );

        String input = "Hello, world!";
        String result = proxy.testMethod(input);

        assertEquals(input, result);
    }

    // Test class for simulating a target instance
    public static class TestClass {
        public String testMethod(String input) {
            return input;
        }
    }

    // Test interface for creating a proxy
    public interface TestInterface {
        String testMethod(String input);
    }
}
