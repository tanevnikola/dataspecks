package com.dataspecks.proxy.core.handler;

import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.commons.exception.unchecked.ArgueException;
import com.dataspecks.proxy.core.AbstractTest;
import com.dataspecks.proxy.core.ProxyBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class RedirectInvocationHandlerTest extends AbstractTest {

    @Test(expected = ArgueException.class)
    public void nullTargetInstanceProvided() {

    }



}
