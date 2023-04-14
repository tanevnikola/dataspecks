package com.dataspecks.proxy.core.base.interceptor;

import com.dataspecks.proxy.builder.BaseBuilder;
import com.dataspecks.proxy.builder.option.*;
import com.dataspecks.proxy.core.base.handler.InvocationContext;
import com.dataspecks.proxy.core.base.handler.InvocationInterceptor;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class InvocationAdapter<K> implements InvocationInterceptor<K> {
    private ValueAdapter resultAdapter = null;
    private List<ValueProducer> argumentProducers = null;
    private Method targetMethod = null;

    @Override
    public Object intercept(InvocationContext<K> ctx) throws Throwable {
        try {
            Object[] finalArgs = Objects.isNull(argumentProducers) ? null : argumentProducers.stream()
                    .map(valueProducer -> valueProducer.produce(ctx.args()))
                    .toArray();
            Object result = ctx.proceed(Objects.isNull(targetMethod) ? ctx.method() : targetMethod, finalArgs);
            return resultAdapter.adapt(result);
        } catch (Throwable t) {
            throw new ProxyInvocationException(String.format("Exception while adapting method call '%s'", ctx.method()), t);
        }
    }

    private void addArgumentProducer(ValueProducer valueProducer) {
        if (Objects.isNull(argumentProducers)) {
            argumentProducers = new ArrayList<>();
        }
        argumentProducers.add(valueProducer);
    }

    /**
     * A builder responsible for building the {@link InvocationAdapter}.
     */
    public static class Builder<K> extends BaseBuilder<InvocationAdapter<K>> implements
            OptionToMethod<ResultAdapterBuilder<K>>,
            OptionPassMethod<ResultAdapterBuilder<K>>{
        protected Builder() {
            super(new InvocationAdapter<>());
        }

        @Override
        public ResultAdapterBuilder<K> toMethod(Method m) {
            getBuildingIstance().targetMethod = m;
            return new ResultAdapterBuilder<>(getBuildingIstance());
        }

        @Override
        public ResultAdapterBuilder<K> passMethod() {
            return new ResultAdapterBuilder<>(getBuildingIstance());
        }
    }

    /**
     * Builder responsible for building the result adapter
     */
    public static class ResultAdapterBuilder<K> extends BaseBuilder<InvocationAdapter<K>> implements
            OptionSetResultAdapter<ArgumentsBuilder<K>>,
            OptionPassResult<ArgumentsBuilder<K>> {

        protected ResultAdapterBuilder(InvocationAdapter<K> buildingInstance) {
            super(buildingInstance);
        }

        /**
         * Sets a {@link ValueAdapter} to adapt the result of the proxy invocation.
         * <p/>
         * The adaption takes place in {@link InvocationAdapter} which is a subclass of {@link InvocationInterceptor}
         * @param valueAdapter the {@link ValueAdapter} to be set
         * @return {@link ArgumentsBuilder} to continue the building process
         */
        @Override
        public ArgumentsBuilder<K> setResultAdapter(ValueAdapter valueAdapter) {
            getBuildingIstance().resultAdapter = valueAdapter;
            return new ArgumentsBuilder<>(getBuildingIstance());
        }

        @Override
        public ArgumentsBuilder<K> passResult() {
            getBuildingIstance().resultAdapter = ValueAdapter.IDENTITY;
            return new ArgumentsBuilder<>(getBuildingIstance());
        }
    }

    /**
     * A builder class with various options for argument building. It is a continuation for the {@link Builder} class.
     */
    public static class ArgumentsBuilder<K> extends BaseBuilder<InvocationAdapter<K>> implements
            OptionForArguments<OptionSetValueProducer<ArgumentsBuilder<K>>>,
            OptionPassArgument<ArgumentsBuilder<K>> {

        private ArgumentsBuilder(InvocationAdapter<K> invocationAdapter) {
            super(invocationAdapter);
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
        public OptionSetValueProducer<ArgumentsBuilder<K>> forArguments(Integer... indexes) {
            return valueProducer -> {
                ValueProducer argumentsSelector = ValueProducer.ARGUMENTS_SELECTOR
                        .forArguments(indexes)
                        .setValueProducer(valueProducer);
                getBuildingIstance().addArgumentProducer(argumentsSelector);
                return this;
            };
        }

        @Override
        public ArgumentsBuilder<K> passArgument() {
            getBuildingIstance().addArgumentProducer(ValueAdapter.IDENTITY);
            return this;
        }

        public InvocationAdapter<K> build() {
            return getBuildingIstance();
        }
    }

    /**
     * @return a builder for {@link InvocationAdapter}
     */
    public static <K> Builder<K> builder() {
        return new Builder<>();
    }
}
