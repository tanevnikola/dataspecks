package com.dataspecks.proxy.base.handler;

import com.dataspecks.builder.exception.runtime.BuilderException;
import com.dataspecks.commons.exception.unchecked.ArgueException;
import com.dataspecks.proxy.base.AbstractTest;
import com.dataspecks.proxy.base.ProxyBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class RedirectInvocationHandlerTest extends AbstractTest {

    @Test(expected = ArgueException.class)
    public void nullTargetInstanceProvided() {
        validateExceptionMessage("Target instance cannot be null",
                () -> RedirectInvocationHandler.builder(null)
                        .build());
    }

    @Test(expected = ArgueException.class)
    public void nullTargetMethodProvided() {
        validateExceptionMessage("Target method cannot be null",
                () -> RedirectInvocationHandler.builder(new TestClass())
                        .build());
    }

    @Test
    public void redirectSuccessfulWithMethodProvided() throws NoSuchMethodException {
        Method m = TestClass.class.getMethod("getTestValue");
        RedirectInvocationHandlerBuilder<TestInterface, TestClass> ihBuilder =
                RedirectInvocationHandler.<TestInterface, TestClass>builder(new TestClass())
                        .setMethod(m);

        TestInterface testInterface = new ProxyBuilder<>(TestInterface.class)
                .setHandler(ihBuilder)
                .build();
        Assert.assertEquals(TESTCLASS_TESTVALUE_DEFAULTVALUE, testInterface.getTestValue());

    }

    @Test(expected = ArgueException.class)
    public void methodDeclaringClassDifferentThanTargetInstanceClass() throws NoSuchMethodException {
        Method m = TestInterface.class.getMethod("getTestValue");
        validateExceptionMessage("Target target method's declaring class is different than the target's instance class",
                () -> RedirectInvocationHandler.<TestInterface, TestClass>builder(new TestClass())
                        .setMethod(m).build());

    }

    @Test
    public void redirectSuccessfulWithMethodLookup() {
        RedirectInvocationHandlerBuilder<TestInterface, TestClass> ihBuilder =
                RedirectInvocationHandler.<TestInterface, TestClass>builder(new TestClass())
                        .setMethod("getTestValue");
        TestInterface testInterface = new ProxyBuilder<>(TestInterface.class)
                .setHandler(ihBuilder)
                .build();
        Assert.assertEquals(TESTCLASS_TESTVALUE_DEFAULTVALUE, testInterface.getTestValue());
    }

    @Test(expected = BuilderException.class)
    public void methodNotFoundWithMethodLookup() {
        validateExceptionMessage("Failed to configure instance 'class com.dataspecks.proxy.base.handler.RedirectInvocationHandler'",
                () -> RedirectInvocationHandler.<TestInterface, TestClass>builder(new TestClass())
                        .setMethod("asd")
                        .build());
    }

    @Test(expected = BuilderException.class)
    public void methodNotFound() {
        validateExceptionMessage("Failed to configure instance 'class com.dataspecks.proxy.base.handler.RedirectInvocationHandler'",
                () -> RedirectInvocationHandler.<TestInterface, TestClass>builder(new TestClass())
                        .setMethod("asd")
                        .build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongNumberOfArguments() {
        RedirectInvocationHandlerBuilder<TestInterface, TestClass> ihBuilder =
                RedirectInvocationHandler.<TestInterface, TestClass>builder(new TestClass())
                        .setMethod("setTestValue", String.class);
        TestInterface testInterface = new ProxyBuilder<>(TestInterface.class)
                .setHandler(ihBuilder)
                .build();
        validateExceptionMessage("wrong number of arguments", testInterface::getTestValue);
    }



}
