package com.dataspecks.commons.core.exception.unchecked;

import java.util.Optional;

public class DsUncheckedException extends RuntimeException {
    public DsUncheckedException() {
        super();
    }

    public DsUncheckedException(String message) {
        super(message);
    }

    public DsUncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DsUncheckedException(Throwable cause) {
        super(cause);
    }

    protected DsUncheckedException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
