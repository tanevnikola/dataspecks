package com.dataspecks.proxy.builder.option;

import java.lang.reflect.InvocationHandler;

public interface OptionSetInvocationHandler<B> extends Option {
    B setInvocationHandler(InvocationHandler val);
}
