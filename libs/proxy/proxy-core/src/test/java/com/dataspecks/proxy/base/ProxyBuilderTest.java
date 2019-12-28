package com.dataspecks.proxy.base;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.exception.runtime.BuilderException;
import com.dataspecks.commons.exception.unchecked.ArgueException;
import org.junit.Test;

public class ProxyBuilderTest extends AbstractTest {
    @Test(expected = ArgueException.class)
    public void testNullTypeProvided() {
        validateExceptionMessage("Proxy type cannot be null", () ->new ProxyBuilder<>(null));
    }

    @Test(expected = ArgueException.class)
    public void nonInterfaceTypeProvided() {
        validateExceptionMessage("'class com.dataspecks.proxy.base.ProxyBuilder' can only be created for interfaces. 'com.dataspecks.proxy.base.ProxyBuilderTest' is not an interface",
                () ->new ProxyBuilder<>(ProxyBuilderTest.class));
    }

    @Test(expected = ArgueException.class)
    public void buildWithNoInvocationHandler() {
        validateExceptionMessage("No InvocationHandler provided", () ->new ProxyBuilder<>(TestInterface.class).build());
    }

    @Test(expected = BuilderException.class)
    public void configurationError() {
        validateExceptionMessage("Failed to create proxy: Configuration failed.",
                () -> new ProxyBuilder<>(TestInterface.class)
                        .setHandler(() -> { throw new RuntimeException(); })
                        .build());
    }

    @Test
    public void checkProxyIsWorking() {
        TestInterface test = new ProxyBuilder<>(TestInterface.class)
                .setHandler(Builder.passThrough((proxy, method, args) -> "Hello World"))
                .build();
        assert test.getTestValue().equals("Hello World");
    }
}
