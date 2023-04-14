package com.dataspecks.proxy.core.base.handler;

import com.dataspecks.proxy.builder.option.OptionIntercept;
import com.dataspecks.proxy.builder.option.OptionSetTargetHandler;
import com.dataspecks.proxy.exception.unchecked.NoHandlerFoundException;
import com.dataspecks.proxy.utils.exception.DsExceptions;

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
public final class DelegatingInvocationHandler<K> extends InterceptableInvocationHandler<K> {

    private static final InvocationHandler DEFAULT_TARGET = (proxy, method, args) -> {
        String message = String.format("No handler found for method '%s'. This is a dead-end invocation.", method);
        throw new NoHandlerFoundException(message);
    };

    private InvocationHandler delegate = DEFAULT_TARGET;

    private DelegatingInvocationHandler() {}

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
        DsExceptions.precondition(invocationHandler != null,
                "Attempted to initialize delegate handler target with a null value, which is not allowed");
        DelegatingInvocationHandler<K> lastDelegateHandler = findLastDelegateHandler();
        if (invocationHandler instanceof DelegatingInvocationHandler) {
            @SuppressWarnings("unchecked")
            DelegatingInvocationHandler<K> targetDelegate = (DelegatingInvocationHandler<K>) invocationHandler;
            if (targetDelegate.isTargetHandlerUninitialized()) {
                targetDelegate.initialize(lastDelegateHandler.delegate);
            }
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
    private boolean isDelegateUninitialized(DelegatingInvocationHandler<K> delegatingInvocationHandler) {
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
    private DelegatingInvocationHandler<K> findLastDelegateHandler() {
        DelegatingInvocationHandler<K> delegateHandler = this;
        while (delegateHandler.delegate instanceof DelegatingInvocationHandler) {
            @SuppressWarnings("unchecked")
            DelegatingInvocationHandler<K> nextDelegate = (DelegatingInvocationHandler<K>) delegateHandler.delegate;
            delegateHandler = nextDelegate;
        }
        return delegateHandler;
    }

    /**
     *
     */
    public static final class Builder<K> implements
            OptionSetTargetHandler<Builder<K>>,
            OptionIntercept<Builder<K>, K> {

        private final DelegatingInvocationHandler<K> delegatingInvocationHandler;
        private final InterceptableInvocationHandler.Builder<K> interceptableInvocationHandlerBuilder;
        protected Builder() {
            delegatingInvocationHandler = new DelegatingInvocationHandler<>();
            interceptableInvocationHandlerBuilder = new InterceptableInvocationHandler.Builder<>(delegatingInvocationHandler);
        }

        @Override
        public Builder<K> setTargetHandler(InvocationHandler targetHandler) {
            delegatingInvocationHandler.initialize(targetHandler);
            return this;
        }

        @Override
        public Builder<K> intercept(InvocationInterceptor<K> interceptor) {
            interceptableInvocationHandlerBuilder.intercept(interceptor);
            return this;
        }

        public DelegatingInvocationHandler<K> build() {
            return delegatingInvocationHandler;
        }
    }

    public static <K> DelegatingInvocationHandler.Builder<K> builder() {
        return new Builder<>();
    }
}
