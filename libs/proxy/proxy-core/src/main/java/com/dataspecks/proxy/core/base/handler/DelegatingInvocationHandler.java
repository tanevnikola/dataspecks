package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionSetTargetHandler;
import com.dataspecks.proxy.utils.exception.DsExceptions;
import com.dataspecks.proxy.utils.handler.base.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class DelegatingInvocationHandler extends DynamicInvocationHandler {
    private static final InvocationHandler DEFAULT_TARGET = InvocationHandlers.DEAD_END;
    private InvocationHandler targetInvocationHandler = DEFAULT_TARGET;

    @Override
    protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
        return targetInvocationHandler.invoke(proxy, method, args);
    }

    /**
     * Initialize this {@link DelegatingInvocationHandler} with a target invocation handler.
     *
     * @param invocationHandler {@link InvocationHandler} to be added as a target to the delegate chain
     */
    public void initialize(InvocationHandler invocationHandler) {
        DsExceptions.precondition(!Objects.isNull(invocationHandler),
                "Attempted to initialize delegate handler target with a null value, which is not allowed");
        DelegatingInvocationHandler lastDelegate = findLastDelegateHandler();
        if (invocationHandler instanceof DelegatingInvocationHandler targetDelegate
                && targetDelegate.isTargetHandlerUninitialized()) {
            targetDelegate.initialize(lastDelegate.targetInvocationHandler);
        } else {
            DsExceptions.precondition(isDelegateUninitialized(lastDelegate),
                    "Attempted to initialize an already initialized target handler, which is not allowed");
        }
        lastDelegate.targetInvocationHandler = invocationHandler;
    }

    /**
     * Check whether the current {@link DelegatingInvocationHandler} is initialized.
     * <p/>
     * It will move to the end of the chain until it finds the {@link DelegatingInvocationHandler} with target that is
     * not of type {@link DelegatingInvocationHandler}. If this target is the <i>"default"</i> invocation handler this
     * means that the delegate has not been initialized.
     *
     * @return true if the delegate is <b>NOT</b> initialized
     */
    public boolean isTargetHandlerUninitialized() {
        return isDelegateUninitialized(findLastDelegateHandler());
    }

    /**
     * Checks whether the provided {@link DelegatingInvocationHandler} is initialized.
     * <p/>
     * If the provided handler has a target that  is the <i>"default"</i> invocation handler this means that the delegate
     * has not been initialized.
     *
     * @param delegatingInvocationHandler {@link DelegatingInvocationHandler} to check
     * @return true if the delegate is <b>NOT</b> initialized
     */
    private boolean isDelegateUninitialized(DelegatingInvocationHandler delegatingInvocationHandler) {
        return Objects.equals(DEFAULT_TARGET, delegatingInvocationHandler.targetInvocationHandler);
    }

    /**
     * Find the last {@link DelegatingInvocationHandler} in the chain.
     * <p/>
     * Each {@link DelegatingInvocationHandler} has a target handler declared as the supertype {@link InvocationHandler}.
     * This method will move through the chain of target handlers until it finds a {@link DelegatingInvocationHandler}
     * whose target handler is not of {@link DelegatingInvocationHandler} type
     *
     * @return the last {@link DelegatingInvocationHandler} in the chain
     */
    private DelegatingInvocationHandler findLastDelegateHandler() {
        DelegatingInvocationHandler delegate = this;
        while (delegate.targetInvocationHandler instanceof DelegatingInvocationHandler) {
            delegate = (DelegatingInvocationHandler) delegate.targetInvocationHandler;
        }
        return delegate;
    }

    /**
     *
     */
    public static class Builder extends DynamicInvocationHandler.Builder<Builder> implements
            OptionSetTargetHandler<Builder> {

        private final DelegatingInvocationHandler delegatingInvocationHandler;

        protected Builder() {
            super(new DelegatingInvocationHandler());
            delegatingInvocationHandler = (DelegatingInvocationHandler) super.build();
        }

        @Override
        public Builder setTargetHandler(InvocationHandler targetHandler) {
            delegatingInvocationHandler.initialize(targetHandler);
            return this;
        }

        public DelegatingInvocationHandler build() {
            return delegatingInvocationHandler;
        }
    }

    public static DelegatingInvocationHandler.Builder builder() {
        return new Builder();
    }
}
