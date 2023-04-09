package com.dataspecks.commons.core.exception.unchecked;

import java.util.Optional;

public class PreconditionViolationException extends DsUncheckedException {
    public PreconditionViolationException() {
        super();
    }

    public PreconditionViolationException(String message) {
        super(message);
    }

    public PreconditionViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreconditionViolationException(Throwable cause) {
        super(cause);
    }

    protected PreconditionViolationException(String message, Throwable cause, boolean enableSuppression,
                                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
