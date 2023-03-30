package com.dataspecks.proxy.utils.handler.extended.adapter;

import com.dataspecks.commons.exception.unchecked.DsUncheckedException;
import com.dataspecks.proxy.core.builder.BuildOptions;
import com.dataspecks.proxy.core.handler.extended.adapter.RewireOperation;
import com.dataspecks.proxy.core.handler.extended.adapter.ValueAdapter;

import java.util.Arrays;
import java.util.Objects;

public final class RewireOperations {
    /**
     * Use a {@link BuildOptions.Set} to create a single-value {@link ValueAdapter} that uses the selected input
     * argument as input to the adapter.
     * Use the {@link BuildOptions.Set#set(Object)} method to pass the {@link ValueAdapter}.
     *
     * @param index the index of the input argument
     * @return {@link BuildOptions.Set}
     */
    static BuildOptions.Set<RewireOperation, ValueAdapter<Object>> withArgument(int index) {
        DsUncheckedException.argue(index >= 0);
        return valueAdapter -> args -> {
            DsUncheckedException.argue(index < args.length);
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
        DsUncheckedException.argue(Objects.nonNull(indices));
        Arrays.stream(indices)
                .forEach(value -> DsUncheckedException.argue(value >= 0));

        return valueAdapter -> args -> {
            DsUncheckedException.argue(Objects.nonNull(args));
            Arrays.stream(indices)
                    .forEach(value -> DsUncheckedException.argue(value < args.length));
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
