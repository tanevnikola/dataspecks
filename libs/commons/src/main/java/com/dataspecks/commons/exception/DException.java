package com.dataspecks.commons.exception;

import com.dataspecks.commons.exception.unchecked.ArgueException;
import com.dataspecks.commons.function.UnsafeRunnable;
import com.dataspecks.commons.function.UnsafeSupplier;

import java.util.Optional;
import java.util.function.Function;

public class DException extends Exception {
    public DException() {
        super();
    }

    public DException(String message) {
        super(message);
    }

    public DException(String message, Throwable cause) {
        super(message, cause);
    }

    public DException(Throwable cause) {
        super(cause);
    }

    protected DException(String message, Throwable cause, boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static <T, E extends Throwable> T getOrThrow(Function<Throwable, E> exF, UnsafeSupplier<T> s) throws E {
        try {
            return s.get();
        } catch (Throwable t) {
            throw exF.apply(t);
        }
    }

    public static <E extends Throwable> void runOrTrow(Function<Throwable, E> exF, UnsafeRunnable<E> r) throws E {
        try {
            r.run();
        } catch (Throwable t) {
            throw exF.apply(t);
        }
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
