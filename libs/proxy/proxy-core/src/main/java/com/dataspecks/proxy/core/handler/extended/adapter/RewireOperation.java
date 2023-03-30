package com.dataspecks.proxy.core.handler.extended.adapter;


/**
 * The {@link RewireOperation} allows a multiple-value {@link ValueAdapter} to be provided that will be used on the
 * input arguments of the call. This enables operation on the input arguments so that they can be modified.
 */
public interface RewireOperation {
    Object rewire(Object... args) throws Throwable;
}