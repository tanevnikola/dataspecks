package com.dataspecks.proxy.builder.option;

import java.lang.reflect.Method;

public interface OptionToMethod<B> extends Option {
    B toMethod(Method m);
}
