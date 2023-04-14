package com.dataspecks.proxy.builder.option;

import com.dataspecks.commons.core.exception.ReflectionException;

public interface OptionToMethod2<B> extends Option {
    B toMethod(String name, Class<?>... argTypes) throws ReflectionException;
}
