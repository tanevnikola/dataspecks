package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.interceptor.ValueAdapter;

public interface OptionSetResultAdapter<B> extends Option {
    /**
     * Setter
     * <p/>
     * <i>This is a generic builder option</i>
     * @param valueAdapter the {@link ValueAdapter} to set
     * @return B (usually a builder, but dependent on the parametrization of the interface)
     */
    B setResultAdapter(ValueAdapter valueAdapter);
}
