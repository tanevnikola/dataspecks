package com.dataspecks.commons.exception.unchecked;

import java.util.Optional;
import java.util.function.Function;

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

    public static void argue(boolean truth) {
        argue(truth, null);
    }

    public static void argue(boolean truth, String msg) {
        argue(t -> null, truth, msg);
    }

    public static <E extends Throwable> void argue(Function<Throwable, E> exF, boolean truth, String msg) {
        if (!truth) {
            throw new ArgueException(String.format("%s", Optional.ofNullable(msg).orElse("<no info>")),
                    exF.apply(null));
        }
    }
}
