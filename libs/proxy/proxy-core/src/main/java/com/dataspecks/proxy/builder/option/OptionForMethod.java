package com.dataspecks.proxy.builder.option;

import java.lang.reflect.Method;

public interface OptionForMethod<B> extends Option {
    B forMethod(Method m);
}
