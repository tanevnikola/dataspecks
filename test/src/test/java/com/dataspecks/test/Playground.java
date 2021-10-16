package com.dataspecks.test;

import com.dataspecks.commons.exception.ReflectionException;
import com.dataspecks.proxy.core.handler.interceptor.adapter.RewireArgumentsInterceptorBuilder;
import com.dataspecks.proxy.core.handler.interceptor.adapter.RewireOperation;
import com.dataspecks.proxy.core.handler.interceptor.adapter.ValueAdapter;
import com.dataspecks.proxy.library.helper.Builders;
import org.junit.Test;


public class Playground {
    private static final String M_NAME_FOO = "foo";
    private static final String M_NAME_GETA = "getA";
    private static final String M_TEST_ARGUMENT_REWIRE = "testArgumentRewire";
    private static final String M_NAME_GETB = "getB";
    private static final String M_NAME_GETC = "getC";

    ImmutableDummyClass dummyClass = new ImmutableDummyClass.Builder()
            .setA(5)
            .setB(7)
            .setC(324)
            .build();

    @Test
    public void newPlayground() throws ReflectionException {
        Builders<DummyClassInterface> builders = Builders.forType(DummyClassInterface.class);

        DummyClassInterface dummyClassInterface = builders.Proxy(builders.InvocationHandler.Dynamic()
                .setStrategy(builders.Strategy.Fallback(dummyClass)
                        .forMethod(M_TEST_ARGUMENT_REWIRE, Integer.class, Boolean.class, String.class).set(
                                builders.InvocationHandler.Interceptable()
                                        .setArgumentsInterceptor(new RewireArgumentsInterceptorBuilder()
                                                .forArgument(0).set(RewireOperation.withArguments(0, 1, 2).set(ValueAdapter.Concatenate)))
                                        .setHandler(builders.InvocationHandler.Redirect(dummyClass)
                                                .setMethod(M_TEST_ARGUMENT_REWIRE, String.class)))

                        .forMethod(M_TEST_ARGUMENT_REWIRE, Boolean.class, Integer.class).set(
                                builders.InvocationHandler.Interceptable()
                                        .setResultInterceptor(ValueAdapter.PassThrough)
                                        .setArgumentsInterceptor(new RewireArgumentsInterceptorBuilder()
                                                .forArgument(0).set(RewireOperation.withArguments(1).set(ValueAdapter.trivial(1)))
                                                .forArgument(1).set(RewireOperation.trivial(true))
                                                .forArgument(2).set(RewireOperation.withArguments(1, 0 ).set(ValueAdapter.Concatenate)))
                                        .setHandler(builders.InvocationHandler.Redirect(dummyClass)
                                                .setMethod(M_TEST_ARGUMENT_REWIRE, Integer.class, Boolean.class, String.class)))
                        .forMethod(M_TEST_ARGUMENT_REWIRE, Integer.class, Boolean.class).set(
                                builders.InvocationHandler.Interceptable()
                                        .setResultInterceptor(ValueAdapter.PassThrough)
                                        .setArgumentsInterceptor(new RewireArgumentsInterceptorBuilder()
                                                .forArgument(0).set(RewireOperation.withArguments(1).set(ValueAdapter.trivial(1)))
                                                .forArgument(1).set(RewireOperation.trivial(true))
                                                .forArgument(2).set(RewireOperation.withArguments(1, 0 ).set(ValueAdapter.using(ValueAdapter.PassThrough))))
                                        .setHandler(builders.InvocationHandler.Redirect(dummyClass)
                                                .setMethod(M_TEST_ARGUMENT_REWIRE, Integer.class, Boolean.class, Object[].class))

                        )
                )
        );
        dummyClassInterface.testArgumentRewire(true, 1);
        dummyClassInterface.testArgumentRewire( 1, true);
    }

}
