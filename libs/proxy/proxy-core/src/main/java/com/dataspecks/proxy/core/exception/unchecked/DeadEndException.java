package com.dataspecks.proxy.core.exception.unchecked;

import com.dataspecks.commons.exception.unchecked.DsUncheckedException;

public class DeadEndException extends DsUncheckedException {
    public DeadEndException() {
        super();
    }

    public DeadEndException(String message) {
        super(message);
    }

    public DeadEndException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeadEndException(Throwable cause) {
        super(cause);
    }

    protected DeadEndException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
