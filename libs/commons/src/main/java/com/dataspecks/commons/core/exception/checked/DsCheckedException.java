package com.dataspecks.commons.core.exception.checked;

public class DsCheckedException extends Exception {
    public DsCheckedException() {
        super();
    }

    public DsCheckedException(String message) {
        super(message);
    }

    public DsCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DsCheckedException(Throwable cause) {
        super(cause);
    }

    protected DsCheckedException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
