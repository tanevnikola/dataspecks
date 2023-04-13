package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.util.Objects;

public interface ValueAdapter extends ValueProducer {
    default Object produce(Object... args) {
        Objects.requireNonNull(args);
        DsExceptions.precondition(args.length == 1, "");
        return adapt(args[0]);
    }

    Object adapt(Object arg);
}
