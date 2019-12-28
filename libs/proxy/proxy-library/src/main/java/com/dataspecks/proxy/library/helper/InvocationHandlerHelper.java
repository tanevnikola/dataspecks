package com.dataspecks.proxy.library.helper;

import com.dataspecks.builder.Builder;
import com.dataspecks.proxy.base.handler.InvocationHandler;
import com.dataspecks.proxy.base.handler.RedirectInvocationHandler;
import com.dataspecks.proxy.base.handler.RedirectInvocationHandlerBuilder;
import com.dataspecks.proxy.base.handler.dynamic.DynamicInvocationHandler;
import com.dataspecks.proxy.base.handler.dynamic.DynamicInvocationHandlerBuilder;
import com.dataspecks.proxy.base.handler.interceptor.InterceptableInvocationHandler;
import com.dataspecks.proxy.base.handler.interceptor.InterceptableInvocationHandlerBuilder;

import java.util.function.Supplier;

public class InvocationHandlerHelper<T> {
    private final Class<T> type;

    public static <T> InvocationHandlerHelper<T> forType(Class<T> type) {
        return new InvocationHandlerHelper<>(type);
    }

    private InvocationHandlerHelper(final Class<T> type) {
        this.type = type;
    }

    /*******************************************************************************************************************
     * Redirect
     ******************************************************************************************************************/

    public <U> RedirectInvocationHandlerBuilder<T, U> Redirect(U target) {
        return RedirectInvocationHandler.builder(target);
    }

    /*******************************************************************************************************************
     * Dynamic
     ******************************************************************************************************************/

    public DynamicInvocationHandlerBuilder<T> Dynamic() {
        return DynamicInvocationHandler.builder();
    }


    /*******************************************************************************************************************
     * Interceptable
     ******************************************************************************************************************/

    public InterceptableInvocationHandlerBuilder<T> Interceptable() {
        return InterceptableInvocationHandler.builder();
    }

    public InterceptableInvocationHandlerBuilder<T> Interceptable(final Builder<? extends InvocationHandler<T>> iHBuilder) {
        return InterceptableInvocationHandler.<T>builder()
                .setHandler(iHBuilder);
    }

    /*******************************************************************************************************************
     * Trivial
     ******************************************************************************************************************/

    public <U> Builder<InvocationHandler<T>> PassThrough(U result) {
        return Builder.passThrough(InvocationHandler.passThrough(result));
    }

    public <U> Builder<InvocationHandler<T>> PassThrough(Supplier<U> resultSupplier) {
        return Builder.passThrough(InvocationHandler.passThrough(resultSupplier));
    }

    public Builder<InvocationHandler<T>> PassThrough(InvocationHandler<T> invocationHandler) {
        return Builder.passThrough(invocationHandler);
    }

    /*******************************************************************************************************************
     * Dead End
     ******************************************************************************************************************/

    public Builder<InvocationHandler<T>> DeadEnd() {
        return Builder.passThrough(InvocationHandler.DeadEnd());
    }
}
