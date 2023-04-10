package com.dataspecks.proxy.core.builder.option;

import java.lang.reflect.Method;

public interface OptionForMethod<O extends Option> extends Option {
    O forMethod(Method m);
}
