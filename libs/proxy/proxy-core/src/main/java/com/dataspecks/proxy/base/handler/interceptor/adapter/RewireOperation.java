package com.dataspecks.proxy.base.handler.interceptor.adapter;


import com.dataspecks.commons.exception.DException;
import com.dataspecks.proxy.base.builder.BuildOptions;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@link RewireOperation} allows a multiple-value {@link ValueAdapter} to be provided that will be used on the
 * input arguments of the call. This enables operation on the input arguments so that they can be modified.
 */
public interface RewireOperation {
    Object perform(Object... args) throws Throwable;


    /**
     * Use a {@link BuildOptions.Set} to build a single-value {@link ValueAdapter} that uses the selected input
     * argument as input to the adapter.
     * Use the {@link BuildOptions.Set#set(Object)} method to pass the {@link ValueAdapter}.
     *
     * @param index the index of the input argument
     * @return {@link BuildOptions.Set}
     */
    static BuildOptions.Set<RewireOperation, ValueAdapter<Object>> withArguments(int index) {
        DException.argue(index >= 0);
        return valueAdapter -> args -> {
            DException.argue(index < args.length);
            return valueAdapter.adapt(args[index]);
        };
    }

    /**
     * Use a {@link BuildOptions.Set} to build a multiple-value {@link ValueAdapter} that uses the selected input
     * arguments as input to the adapter.
     * Use the {@link BuildOptions.Set#set(Object)} method to pass the {@link ValueAdapter}.
     *
     * @param indices an array of input arguments indices
     * @return {@link BuildOptions.Set}
     */
    static BuildOptions.Set<RewireOperation, ValueAdapter<Object[]>> withArguments(int... indices) {
        DException.argue(Objects.nonNull(indices));
        Arrays.stream(indices)
                .forEach(value -> DException.argue(value >= 0));

        return valueAdapter -> args -> {
            Arrays.stream(indices)
                    .forEach(value -> DException.argue(value < args.length));
            Object[] argsToPass = Arrays.stream(indices)
                    .mapToObj(i -> args[i])
                    .toArray();
            return valueAdapter.adapt(argsToPass);
        };
    }

    /**
     * Build a trivial {@link RewireOperation} that will always return the provided value
     *
     * @param value the value to be returned by this operation
     * @return a {@link RewireOperation}
     */
    static <T> RewireOperation trivial(T value) {
        return (args) -> value;
    }
}