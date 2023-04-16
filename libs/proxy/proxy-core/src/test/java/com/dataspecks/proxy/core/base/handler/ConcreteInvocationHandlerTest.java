package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.base.registry.InstanceRegistry;
import com.dataspecks.test.MethodsMockUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ConcreteInvocationHandlerTest {
    private ConcreteInvocationHandler<Method> concreteInvocationHandlerSpy;
    private InstanceRegistry<Method> instanceRegistryMock;

    private final Method matchedMethodMockA = Mockito.mock(Method.class);
    private final Dummy matchedInstanceMockA = Mockito.mock(Dummy.class);
    private final Method matchedMethodMockB = Mockito.mock(Method.class);
    private final Dummy matchedInstanceMockB = Mockito.mock(Dummy.class);

    private final Method keyMockA = Mockito.mock(Method.class);
    private final Method keyMockB = Mockito.mock(Method.class);

    private final Object methodInvokeResultMockMethodAResultA = Mockito.mock(Object.class);
    private final Object methodInvokeResultMockMethodAResultB = Mockito.mock(Object.class);

    // proxy invoke arguments
    private final Object proxyParameterProxyMock = Mockito.mock(Object.class);
    private final Method proxyParameterMethodMock = Mockito.mock(Method.class);
    private final Object[] proxyParameterArgumentsA = new Object[]{1, 2};;
    private final Object[] proxyParameterArgumentsB = new Object[]{1, 3};;

    private MockedStatic<Methods> methods;


    static class Dummy {

    }

    static class TestRegistry extends InstanceRegistry<Method> {

        @Override
        protected Object computeValue(Method key, Object currentValue) {
            return null;
        }

        @Override
        public Method resolveKey(Object proxy, Method method, Object[] args) {
            return null;
        }
    }

    @BeforeEach
    void initTest(TestInfo testInfo) {
        System.out.printf("Executing test '%s'\n", testInfo.getDisplayName());

        // mock InstanceRegistry
        //////////////////////////////////////////////////////////////////////
        instanceRegistryMock = Mockito.mock(TestRegistry.class);

        // mock ConcreteInvocationHandler
        //////////////////////////////////////////////////////////////////////
        concreteInvocationHandlerSpy = Mockito.spy(new ConcreteInvocationHandler<>());
        Mockito.doReturn(instanceRegistryMock)
                .when(concreteInvocationHandlerSpy)
                .getInstanceRegistry(proxyParameterProxyMock);

        // mock the statics
        //////////////////////////////////////////////////////////////////////
        methods = Mockito.mockStatic(Methods.class);

        methods.when(() -> Methods.getMatching(matchedInstanceMockA.getClass(), proxyParameterMethodMock))
                .thenReturn(matchedMethodMockA);
        methods.when(() -> Methods.getMatching(matchedInstanceMockB.getClass(), proxyParameterMethodMock))
                .thenReturn(matchedMethodMockB);

        // mock invoke to return 2 different result when invoked with 2 different params
        methods.when(() -> Methods.invoke(
                Mockito.eq(matchedInstanceMockA),
                Mockito.eq(matchedMethodMockA),
                Mockito.eq(proxyParameterArgumentsA)
        ))
                .thenReturn(methodInvokeResultMockMethodAResultA);

        methods.when(() -> Methods.invoke(
                Mockito.eq(matchedInstanceMockA),
                Mockito.eq(matchedMethodMockA),
                Mockito.eq(proxyParameterArgumentsB)
        ))
                .thenReturn(methodInvokeResultMockMethodAResultB);


        Mockito.when(instanceRegistryMock.resolveKey(
                proxyParameterProxyMock,
                proxyParameterMethodMock,
                proxyParameterArgumentsA
        ))
                .thenReturn(keyMockA);
        Mockito.when(instanceRegistryMock.find(keyMockA))
                .thenReturn(matchedInstanceMockA);

        Mockito.when(instanceRegistryMock.resolveKey(
                proxyParameterProxyMock,
                proxyParameterMethodMock,
                proxyParameterArgumentsB
        ))
                .thenReturn(keyMockA);
        Mockito.when(instanceRegistryMock.find(keyMockB))
                .thenReturn(matchedInstanceMockB);





/*
        methods.when(() -> Methods.invoke(
                matchedInstanceMock,
                matchedMethodMock,
                methodInvokeResultMockA))
                .then(dasdsa -> methodInvokeResultMockA);

 */
        MethodsMockUtils.MethodCall a = new MethodsMockUtils.MethodCall(proxyParameterProxyMock, proxyParameterMethodMock, 1, 2);
        MethodsMockUtils.MethodCall b = new MethodsMockUtils.MethodCall(proxyParameterProxyMock, proxyParameterMethodMock, 1, 2);
        a.equals(b);
    }

    @AfterEach
    void teardownTest(TestInfo testInfo) {
        System.out.printf("Test done '%s'\n", testInfo.getDisplayName());
    }

    @DisplayName("Check cache change on invocation")
    @Test
    public void testTest() throws Throwable {


        Object resultA = concreteInvocationHandlerSpy.invoke(proxyParameterProxyMock, proxyParameterMethodMock, proxyParameterArgumentsB);
        Assertions.assertEquals(methodInvokeResultMockMethodAResultB, resultA);

        Field privateField = concreteInvocationHandlerSpy.getClass().getDeclaredField("cachedInstance");
        privateField.setAccessible(true);
        privateField.get(concreteInvocationHandlerSpy);
    }
}