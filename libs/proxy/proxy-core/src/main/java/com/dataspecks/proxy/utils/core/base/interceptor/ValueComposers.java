package com.dataspecks.proxy.utils.core.base.interceptor;

import com.dataspecks.proxy.builder.option.OptionForArguments;
import com.dataspecks.proxy.builder.option.OptionSetValueComposer;
import com.dataspecks.proxy.core.base.interceptor.ValueComposer;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.util.Objects;

public final class ValueComposers {
    private ValueComposers() {}

    public static OptionForArguments<OptionSetValueComposer<ValueComposer>> ARGUMENTS_SELECTOR =
            indexes -> valueComposer -> args ->
    {
        Objects.requireNonNull(args);
        Object[] finalArgs = null;
        if(Objects.nonNull(indexes)) {
            finalArgs = new Object[indexes.length];
            int i = 0;
            for (Integer index : indexes) {
                DsExceptions.precondition(index < args.length);
                finalArgs[i++] = args[index];
            }
        }
        return valueComposer.compose(finalArgs);
    };

}
