package com.dataspecks.commons.core.exception.unchecked;

import java.util.Optional;

public class ArgueException extends DsUncheckedException {
    public ArgueException() {
        super();
    }

    public ArgueException(String message) {
        super(message);
    }

    public ArgueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgueException(Throwable cause) {
        super(cause);
    }

    protected ArgueException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
