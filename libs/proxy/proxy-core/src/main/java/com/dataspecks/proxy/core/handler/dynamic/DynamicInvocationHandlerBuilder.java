package com.dataspecks.proxy.core.handler.dynamic;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.proxy.core.builder.contract.handler.dynamic.DynamicInvocationHandlerBuildContract;

import java.util.Objects;

/**
 * Concrete builder
 * @param <T> proxy type
 */
public final class DynamicInvocationHandlerBuilder<T>
        extends GenericBuilder<DynamicInvocationHandler<T>>
        implements  Builder<DynamicInvocationHandler<T>>, DynamicInvocationHandlerBuildContract<T, DynamicInvocationHandlerBuilder<T>> {

    public DynamicInvocationHandlerBuilder() {
        super(DynamicInvocationHandler::new);
    }

    @Override
    public DynamicInvocationHandlerBuilder<T> setStrategy(Builder<? extends InvocationStrategy<T>> iSBuilder) {
        DException.argue(Objects.nonNull(iSBuilder));
        configure(dIHandler -> dIHandler.setiStrategy(iSBuilder.build()));
        return this;
    }

    /**
     * Validate the instance
     * @param dIHandler {@link DynamicInvocationHandler}
     * @return {@link DynamicInvocationHandler
     */
    protected DynamicInvocationHandler<T> validate(DynamicInvocationHandler<T> dIHandler) {
        DException.argue(Objects.nonNull(dIHandler.getiStrategy()));
        return super.validate(dIHandler);
    }
}
