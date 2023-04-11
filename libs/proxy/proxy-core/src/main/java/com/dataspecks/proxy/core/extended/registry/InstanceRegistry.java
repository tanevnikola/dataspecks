package com.dataspecks.proxy.core.extended.registry;

import com.dataspecks.commons.core.exception.ReflectionException;
import com.dataspecks.commons.utils.reflection.Methods;
import com.dataspecks.proxy.core.base.registry.Registry;
import com.dataspecks.proxy.exception.unchecked.ProxyInvocationException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public interface InstanceRegistry<K> extends Registry<Object, K> {
}
