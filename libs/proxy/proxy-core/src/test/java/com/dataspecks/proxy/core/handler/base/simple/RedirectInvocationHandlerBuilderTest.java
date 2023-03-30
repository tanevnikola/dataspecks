package com.dataspecks.proxy.core.handler.base.simple;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class RedirectInvocationHandlerBuilderTest {
    private TestClass targetInstance;

    @Before
    public void setUp() {
        targetInstance = new TestClass();
    }

    @Test
    public void testBuilder_withValidInstanceAndMethod() throws Exception {
        InvocationHandler handler = new RedirectInvocationHandlerBuilder<>(targetInstance)
                .fromMethod("testMethod", String.class);

        assertTrue(handler instanceof RedirectInvocationHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilder_withNullInstance() {
        new RedirectInvocationHandlerBuilder<TestClass>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilder_withInvalidMethod() throws Exception {
        Method invalidMethod = AnotherClass.class.getDeclaredMethod("anotherMethod", String.class);

        new RedirectInvocationHandlerBuilder<>(targetInstance)
                .fromMethod(invalidMethod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilder_withoutSettingMethod() {
        new RedirectInvocationHandlerBuilder<>(targetInstance)
                .build();
    }

    // Test class for simulating a target instance
    public static class TestClass {
        public String testMethod(String input) {
            return input;
        }
    }

    // Another class for simulating an invalid method
    public static class AnotherClass {
        public String anotherMethod(String input) {
            return input;
        }
    }
}
