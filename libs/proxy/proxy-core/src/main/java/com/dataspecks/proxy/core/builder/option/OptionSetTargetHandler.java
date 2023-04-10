package com.dataspecks.proxy.core.builder.option;

import java.lang.reflect.InvocationHandler;

public interface OptionSetTargetHandler<B> extends Option {
    B setTargetHandler(InvocationHandler targetHandler);
}