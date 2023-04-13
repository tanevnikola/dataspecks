package com.dataspecks.proxy.builder.option;

import com.dataspecks.commons.core.exception.ReflectionException;

public interface OptionForMethod2<B> extends Option {
    B forMethod(String name, Class<?>... argTypes) throws ReflectionException;
}