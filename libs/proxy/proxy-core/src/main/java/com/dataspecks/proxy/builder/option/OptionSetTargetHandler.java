package com.dataspecks.proxy.builder.option;

import java.lang.reflect.InvocationHandler;

public interface OptionSetTargetHandler<B> extends Option {
    /**
     * Setter
     * <p/>
     * <i>This is a generic builder option</i>
     * @param targetHandler the {@link InvocationHandler} to set
     * @return B (usually a builder, but dependent on the parametrization of the interface)
     */
    B setTargetHandler(InvocationHandler targetHandler);
}