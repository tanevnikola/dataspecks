package com.dataspecks.proxy.builder.option;

import com.dataspecks.proxy.core.base.registry.Registry;

public interface OptionSetRegistry<B, R extends Registry<?, ?>> extends Option {
    B setRegistry(R registry);
}
