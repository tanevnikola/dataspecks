package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.builder.option.OptionAddArgumentComposer;
import com.dataspecks.proxy.builder.option.OptionSetResultAdapter;
import com.dataspecks.proxy.core.base.handler.InvocationContext;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvocationAdapter implements InvocationInterceptor {
    private ValueAdapter valueAdapter = null;
    private final List<ValueComposer> valueComposers = new ArrayList<>();

    @Override
    public Object intercept(InvocationContext ctx) throws Throwable {
        Object[] finalArgs = adaptArguments(ctx.args());
        Object result = ctx.proceed(ctx.method(), finalArgs);
        return valueAdapter.adapt(result);
    }

    private Object[] adaptArguments(Object[] args) {
        Objects.requireNonNull(args);
        return valueComposers.stream()
                .map(valueComposer -> valueComposer.adapt(args))
                .toArray();
    }

    private record ArgumentComposer(List<Integer> selectedArguments, ValueComposer valueComposer) {
        public Object compose(Object[] args) {
            return null;
        }
    }

    /**
     *
     */
    public static class Builder implements
            OptionSetResultAdapter<Builder.ArgumentsBuilder> {
        private final InvocationAdapter invocationAdapter = new InvocationAdapter();

        @Override
        public ArgumentsBuilder setResultAdapter(ValueAdapter valueAdapter) {
            invocationAdapter.valueAdapter = valueAdapter;
            return new ArgumentsBuilder(invocationAdapter);
        }

        public static class ArgumentsBuilder implements
                OptionAddArgumentComposer<ArgumentsBuilder> {

            public ArgumentsBuilder(InvocationAdapter invocationAdapter) {
                this.invocationAdapter = invocationAdapter;
            }

            private final InvocationAdapter invocationAdapter;

            @Override
            public ArgumentsBuilder addArgumentComposer(ValueComposer valueComposer) {
                invocationAdapter.valueComposers.add(valueComposer);
                return this;
            }

            public InvocationAdapter build() {
                return invocationAdapter;
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
