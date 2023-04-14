package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.util.Objects;

public interface ValueAdapter extends ValueProducer {
    default Object produce(Object... args) {
        Objects.requireNonNull(args, "Value adapter called with 0 arguments");
        DsExceptions.precondition(args.length == 1, "Invalid call to value adapter. Args must be an array of 1 element");
        return adapt(args[0]);
    }

    Object adapt(Object arg);

    ValueAdapter IDENTITY = arg -> arg;
}
