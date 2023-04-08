package com.dataspecks.proxy.exception.unchecked;

import com.dataspecks.commons.core.exception.unchecked.DsUncheckedException;

public class NoHandlerFoundException extends DsUncheckedException {
    public NoHandlerFoundException() {
        super();
    }

    public NoHandlerFoundException(String message) {
        super(message);
    }

    public NoHandlerFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoHandlerFoundException(Throwable cause) {
        super(cause);
    }

    protected NoHandlerFoundException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
