package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.interceptor.ValueProducer;

public interface OptionSetValueProducer<B> extends Option {
    /**
     * Setter
     * <p/>
     * <i>This is a generic builder option</i>
     * @param valueProducer the {@link ValueProducer} to set
     * @return B (usually a builder, but dependent on the parametrization of the interface)
     */
    B setValueProducer(ValueProducer valueProducer);
}
