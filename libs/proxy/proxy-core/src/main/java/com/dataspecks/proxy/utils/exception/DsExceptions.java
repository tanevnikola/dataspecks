package com.dataspecks.proxy.utils.exception;

import com.dataspecks.commons.core.exception.unchecked.PreconditionViolationException;

import java.util.Optional;

public class DsExceptions {
    public static void ensure(boolean truth) {
        ensure(truth, null);
    }

    public static void ensure(boolean truth, String msg) {
        if (!truth) {
            throw new PreconditionViolationException(String.format("%s", Optional.ofNullable(msg).orElse("<no info>")));
        }
    }
}
