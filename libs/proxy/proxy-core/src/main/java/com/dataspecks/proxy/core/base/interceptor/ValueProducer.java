package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.builder.option.OptionForArguments;
import com.dataspecks.proxy.builder.option.OptionSetValueProducer;
import com.dataspecks.proxy.utils.exception.DsExceptions;

import java.util.Objects;

public interface ValueProducer {
    Object produce(Object... args);

    OptionForArguments<OptionSetValueProducer<ValueProducer>> ARGUMENTS_SELECTOR =
            indexes -> valueProducer -> args ->
            {
                Object[] finalArgs = null;
                if(Objects.nonNull(indexes) && Objects.nonNull(args)) {
                    finalArgs = new Object[indexes.length];
                    int i = 0;
                    for (Integer index : indexes) {
                        DsExceptions.precondition(index < args.length);
                        finalArgs[i++] = args[index];
                    }
                }
                return valueProducer.produce(finalArgs);
            };
}
