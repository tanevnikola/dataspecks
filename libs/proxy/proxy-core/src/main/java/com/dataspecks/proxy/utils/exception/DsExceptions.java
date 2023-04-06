package com.dataspecks.proxy.utils.exception;

import com.dataspecks.commons.core.exception.unchecked.ArgueException;

import java.util.Optional;
import java.util.function.Supplier;

public class DsExceptions {
    public static void argue(boolean truth) {
        argue(truth, null);
    }

    public static void argue(boolean truth, String msg) {
        if (!truth) {
            throw new ArgueException(String.format("%s", Optional.ofNullable(msg).orElse("<no info>")));
        }
    }
}
