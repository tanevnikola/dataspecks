package com.dataspecks.proxy.base.exception;

public class ProxyInvocationException extends ProxyException {
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
