package com.dataspecks.proxy.core.exception.unchecked;

import com.dataspecks.commons.exception.unchecked.UncheckedException;

public class ValueAdapterException extends UncheckedException {
    public ValueAdapterException() {
        super();
    }

    public ValueAdapterException(String message) {
        super(message);
    }

    public ValueAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueAdapterException(Throwable cause) {
        super(cause);
    }

    protected ValueAdapterException(String message, Throwable cause, boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
