package com.dataspecks.proxy.core.builder.option;

import com.dataspecks.proxy.core.handler.registry.Registry;

public interface OptionSetRegistry<B, R extends Registry<?, ?>> extends Option {
    B setRegistry(R registry);
}
