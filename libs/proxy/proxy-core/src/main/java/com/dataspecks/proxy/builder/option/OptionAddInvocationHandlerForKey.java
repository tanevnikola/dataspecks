package com.dataspecks.proxy.builder.option;

import java.lang.reflect.InvocationHandler;

public interface OptionAddInvocationHandlerForKey<B, K> extends Option {
    B addInvocationHandler(K key, InvocationHandler invocationHandler);
}
