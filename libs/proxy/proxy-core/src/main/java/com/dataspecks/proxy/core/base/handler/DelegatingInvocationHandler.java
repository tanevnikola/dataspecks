package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionSetTargetHandler;
import com.dataspecks.proxy.utils.exception.DsExceptions;
import com.dataspecks.proxy.utils.core.base.handler.InvocationHandlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * {@link DelegatingInvocationHandler} is a {@link InterceptableInvocationHandler} subclass that delegates the call to a
 * delegate declared as the supertype {@link InvocationHandler}. On creation and before initialization it is an
 * "empty shell", containing a "default" delegate that will throw {@link com.dataspecks.proxy.exception.unchecked.NoHandlerFoundException} exception
 * if invoked. On initialization an {@link InvocationHandler} is provided which will replace the "default" delegate.
 * If the provided {@link InvocationHandler} is of type {@link DelegatingInvocationHandler} it will be added as the next
 * delegate thus allowing the creation of delegate chains.
 * <p/>
 * The chain is considered initialized if the last delegate in the chain is not of {@link DelegatingInvocationHandler} type
 * and is not "default" delegate. Initializing an already initialized chain will result in a {@link com.dataspecks.commons.core.exception.unchecked.PreconditionViolationException}
 * <p/>
 * Chained delegates are invoked recursively.
 * <p/>
 * It is important to note that if initialized with another {@link DelegatingInvocationHandler} the provided delegate will
 * always go at the end of the chain. This assures "natural" order of invocation such as the last {@link DelegatingInvocationHandler}
 * will be invoked last
 * <p/>
 * Example - initialized chain. Note the concrete handler at the end:
 * <pre> {@code
 *         DelegatingInvocationHandler delegateRoot = DelegatingInvocationHandler.builder()
 *                 .setTargetHandler(DelegatingInvocationHandler.builder()
 *                         .setTargetHandler(DelegatingInvocationHandler.builder()
 *                                 // concrete handler
 *                                 .setTargetHandler((proxy, method, args) -> 1)
 *                                 .build())
 *                         .build())
 *                 .build();
 * }</pre>
 */
public final class DelegatingInvocationHandler extends InterceptableInvocationHandler {
    private static final InvocationHandler DEFAULT_TARGET = InvocationHandlers.DEAD_END;
    private InvocationHandler delegate = DEFAULT_TARGET;

    @Override
    protected Object proceed(Object proxy, Method method, Object[] args) throws Throwable {
        return delegate.invoke(proxy, method, args);
    }

    /**
     * Initialize this {@link DelegatingInvocationHandler} with an {@link InvocationHandler}.
     *
     * @param invocationHandler {@link InvocationHandler} to be added as a target to the delegate chain
     */
    public void initialize(InvocationHandler invocationHandler) {
        DsExceptions.precondition(!Objects.isNull(invocationHandler),
                "Attempted to initialize delegate handler target with a null value, which is not allowed");
        DelegatingInvocationHandler lastDelegateHandler = findLastDelegateHandler();
        if (invocationHandler instanceof DelegatingInvocationHandler targetDelegate
                && targetDelegate.isTargetHandlerUninitialized()) {
            targetDelegate.initialize(lastDelegateHandler.delegate);
        } else {
            DsExceptions.precondition(isDelegateUninitialized(lastDelegateHandler),
                    "Attempted to initialize an already initialized target handler, which is not allowed");
        }
        lastDelegateHandler.delegate = invocationHandler;
    }

    /**
     * Check whether the current {@link DelegatingInvocationHandler} is initialized.
     * <p/>
     * It will move to the end of the chain until it finds the {@link DelegatingInvocationHandler} with delegate that is
     * not of type {@link DelegatingInvocationHandler}. If this delegate is the <i>"default"</i> invocation handler this
     * means that the delegate handler has not been initialized.
     *
     * @return true if the delegate handler is <b>NOT</b> initialized
     */
    public boolean isTargetHandlerUninitialized() {
        return isDelegateUninitialized(findLastDelegateHandler());
    }

    /**
     * Checks whether the provided {@link DelegatingInvocationHandler} is initialized.
     * <p/>
     * If the provided handler has a delegate that is the <i>"default"</i> invocation handler this means that the
     * delegate handler has not been initialized.
     *
     * @param delegatingInvocationHandler {@link DelegatingInvocationHandler} to check
     * @return true if the delegate handler is <b>NOT</b> initialized
     */
    private boolean isDelegateUninitialized(DelegatingInvocationHandler delegatingInvocationHandler) {
        return Objects.equals(DEFAULT_TARGET, delegatingInvocationHandler.delegate);
    }

    /**
     * Find the last {@link DelegatingInvocationHandler} in the chain.
     * <p/>
     * Each {@link DelegatingInvocationHandler} has a delegate declared as the supertype {@link InvocationHandler}.
     * This method will move through the chain of delegates until it finds one whose delegate is not of type
     * {@link DelegatingInvocationHandler}
     *
     * @return the last {@link DelegatingInvocationHandler} in the chain
     */
    private DelegatingInvocationHandler findLastDelegateHandler() {
        DelegatingInvocationHandler delegateHandler = this;
        while (delegateHandler.delegate instanceof DelegatingInvocationHandler) {
            delegateHandler = (DelegatingInvocationHandler) delegateHandler.delegate;
        }
        return delegateHandler;
    }

    /**
     *
     */
    public static class Builder extends InterceptableInvocationHandler.Builder<Builder> implements
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
