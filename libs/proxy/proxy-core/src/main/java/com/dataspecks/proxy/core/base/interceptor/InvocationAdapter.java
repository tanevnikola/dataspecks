package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.builder.option.OptionForArguments;
import com.dataspecks.proxy.builder.option.OptionSetResultAdapter;
import com.dataspecks.proxy.builder.option.OptionSetValueProducer;
import com.dataspecks.proxy.core.base.handler.InvocationContext;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;

import java.util.ArrayList;
import java.util.List;

public final class InvocationAdapter implements InvocationInterceptor {
    private ValueAdapter valueAdapter = null;
    private final List<ValueProducer> valueProducers = new ArrayList<>();

    @Override
    public Object intercept(InvocationContext ctx) throws Throwable {
        Object[] finalArgs = valueProducers.stream()
                .map(valueProducer -> valueProducer.produce(ctx.args()))
                .toArray();
        Object result = ctx.proceed(ctx.method(), finalArgs);
        return valueAdapter.adapt(result);
    }

    /**
     * A builder responsible for building the {@link InvocationAdapter}.
     */
    public static class Builder implements
            OptionSetResultAdapter<ArgumentsBuilder> {
        private final InvocationAdapter invocationAdapter = new InvocationAdapter();

        /**
         * Sets a {@link ValueAdapter} to adapt the result of the proxy invocation.
         * <p/>
         * The adaption takes place in {@link InvocationAdapter} which is a subclass of {@link InvocationInterceptor}
         * @param valueAdapter the {@link ValueAdapter} to be set
         * @return {@link ArgumentsBuilder} to continue the building process
         */
        @Override
        public ArgumentsBuilder setResultAdapter(ValueAdapter valueAdapter) {
            invocationAdapter.valueAdapter = valueAdapter;
            return new ArgumentsBuilder(invocationAdapter);
        }
    }

    /**
     * A builder class with various options for argument building. It is a continuation for the {@link Builder} class.
     */
    public static class ArgumentsBuilder implements
            OptionForArguments<OptionSetValueProducer<ArgumentsBuilder>> {

        private final InvocationAdapter invocationAdapter;

        private ArgumentsBuilder(InvocationAdapter invocationAdapter) {
            this.invocationAdapter = invocationAdapter;
        }

        /**
         * Select arguments of the proxy invocation
         *
         * @param indexes indexes of the arguments to select
         * @return {@link OptionSetValueProducer} to set a {@link ValueProducer} that will receive the selected arguments
         * during the interception of the proxy invocation.
         * <p/>
         * The provided {@link ValueProducer} will produce a single argument value for the {@link InvocationAdapter}
         */
        @Override
        public OptionSetValueProducer<ArgumentsBuilder> forArguments(Integer... indexes) {
            return valueProducer -> {
                ValueProducer argumentsSelector = ValueProducer.ARGUMENTS_SELECTOR
                        .forArguments(indexes)
                        .setValueProducer(valueProducer);
                invocationAdapter.valueProducers.add(argumentsSelector);
                return this;
            };
        }

        public InvocationAdapter build() {
            return invocationAdapter;
        }
    }

    /**
     * @return a builder for {@link InvocationAdapter}
     */
    public static Builder builder() {
        return new Builder();
    }
}
