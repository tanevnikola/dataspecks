package com.dataspecks.proxy.builder.option;

import com.dataspecks.commons.core.exception.ReflectionException;

public interface OptionForMethod2<O extends Option> extends Option {
    O forMethod(String name, Class<?>... argTypes) throws ReflectionException;
}