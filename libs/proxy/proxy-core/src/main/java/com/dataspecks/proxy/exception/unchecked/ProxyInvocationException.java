package com.dataspecks.proxy.exception.unchecked;

import com.dataspecks.commons.core.exception.checked.DsCheckedException;

public class ProxyInvocationException extends DsCheckedException {
    public ProxyInvocationException() {
        super();
    }

    public ProxyInvocationException(String message) {
        super(message);
    }

    public ProxyInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyInvocationException(Throwable cause) {
        super(cause);
    }

    protected ProxyInvocationException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}