package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.builder.option.OptionForArguments;
import com.dataspecks.proxy.builder.option.OptionSetResultAdapter;
import com.dataspecks.proxy.builder.option.OptionSetValueComposer;
import com.dataspecks.proxy.core.base.handler.InvocationContext;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.utils.core.base.interceptor.ValueComposers;

import java.util.ArrayList;
import java.util.List;

public final class InvocationAdapter implements InvocationInterceptor {
    private ValueAdapter valueAdapter = null;
    private final List<ValueComposer> valueComposers = new ArrayList<>();

    @Override
    public Object intercept(InvocationContext ctx) throws Throwable {
        Object[] finalArgs = valueComposers.stream()
                .map(valueComposer -> valueComposer.compose(ctx.args()))
                .toArray();
        Object result = ctx.proceed(ctx.method(), finalArgs);
        return valueAdapter.adapt(result);
    }

    /**
     *
     */
    public static class Builder implements
            OptionSetResultAdapter<ArgumentsBuilder> {
        private final InvocationAdapter invocationAdapter = new InvocationAdapter();

        @Override
        public ArgumentsBuilder setResultAdapter(ValueAdapter valueAdapter) {
            invocationAdapter.valueAdapter = valueAdapter;
            return new ArgumentsBuilder(invocationAdapter);
        }
    }

    /**
     *
     */
    public static class ArgumentsBuilder implements
            OptionForArguments<OptionSetValueComposer<ArgumentsBuilder>> {

        private final InvocationAdapter invocationAdapter;

        private ArgumentsBuilder(InvocationAdapter invocationAdapter) {
            this.invocationAdapter = invocationAdapter;
        }

        @Override
        public OptionSetValueComposer<ArgumentsBuilder> forArguments(Integer... indexes) {
            return valueComposer -> {
                ValueComposer argumentsSelector = ValueComposers.ARGUMENTS_SELECTOR
                        .forArguments(indexes)
                        .setValueComposer(valueComposer);
                invocationAdapter.valueComposers.add(argumentsSelector);
                return this;
            };
        }

        public InvocationAdapter build() {
            return invocationAdapter;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
