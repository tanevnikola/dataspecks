package com.dataspecks.proxy.core.handler.extended.adapter;

/**
 * The ValueAdapter interface provides a mechanism to adapt or convert values.
 *
 * @param <T> the type of the value to be adapted; not necessarily the actual type of the value, but rather a means
 *           to distinguish between methods intended for Object[] versus Object
 */
public interface ValueAdapter<T> {

    /**
     * Defines the contract for adapting a given value.
     *
     * @param val the value to be adapted
     * @return the adapted value, as a result of applying the function F(val)
     * @throws Throwable if any exception occurs during the adaptation process
     */
    Object adapt(T val) throws Throwable;

}
