package com.dataspecks.proxy.core;

import com.dataspecks.commons.exception.unchecked.ArgueException;
import org.junit.Test;

public class ProxyBuilderTest extends AbstractTest {
    @Test(expected = ArgueException.class)
    public void testNullTypeProvided() {
    }

}
