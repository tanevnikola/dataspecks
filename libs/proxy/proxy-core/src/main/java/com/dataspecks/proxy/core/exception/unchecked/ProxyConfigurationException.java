package com.dataspecks.proxy.core.exception.unchecked;

import com.dataspecks.commons.core.exception.unchecked.DsUncheckedException;

public class ProxyConfigurationException extends DsUncheckedException {
    public ProxyConfigurationException() {
        super();
    }

    public ProxyConfigurationException(String message) {
        super(message);
    }

    public ProxyConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyConfigurationException(Throwable cause) {
        super(cause);
    }

    protected ProxyConfigurationException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
