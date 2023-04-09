package com.dataspecks.proxy.core.handler.base;

import com.dataspecks.proxy.utils.exception.DsExceptions;
import com.dataspecks.proxy.utils.handler.base.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DelegatingInvocationHandler extends DynamicInvocationHandler {
    private static final InvocationHandler DEFAULT_DELEGATE = InvocationHandlers.DEAD_END;
    private final AtomicReference<InvocationHandler> targetHandler = new AtomicReference<>(DEFAULT_DELEGATE);

    public void initialize(InvocationHandler delegateHandler) {
        DsExceptions.ensure(!Objects.isNull(delegateHandler),
                "Attempted to initialize delegate handler target with a null value, which is not allowed");
        findLastTargetHandler(this.targetHandler).compareAndSet(DEFAULT_DELEGATE, delegateHandler);
    }

    public boolean isTargetHandlerUninitialized() {
        return DEFAULT_DELEGATE.equals(findLastTargetHandler(targetHandler).get());
    }

    @Override
    protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
        return targetHandler.get().invoke(proxy, method, args);
    }

    private AtomicReference<InvocationHandler> findLastTargetHandler(AtomicReference<InvocationHandler> handler) {
        Objects.requireNonNull(handler);
        while (handler.get() instanceof DelegatingInvocationHandler nextHandler) {
            handler = nextHandler.targetHandler;
        }
        return handler;
    }

    /**
     *
     */
    public static class Builder extends DynamicInvocationHandler.Builder<Builder> {
        private final DelegatingInvocationHandler delegatingInvocationHandler;

        public Builder setDelegate(InvocationHandler delegateHandler) {
            delegatingInvocationHandler.initialize(delegateHandler);
            return this;
        }

        protected Builder() {
            super(new DelegatingInvocationHandler());
            delegatingInvocationHandler = (DelegatingInvocationHandler) super.build();
        }

        public DelegatingInvocationHandler build() {
            return delegatingInvocationHandler;
        }
    }

    public static DelegatingInvocationHandler.Builder builder() {
        return new Builder();
    }
}
