package com.dataspecks.commons.utils.reflection;

import com.dataspecks.commons.core.exception.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class Methods {
    public static Method findMatching(Class<?> targetType, Method mToMatch) {
        try {
            return getMatching(targetType, mToMatch);
        } catch (ReflectionException e) {
            return null;
        }
    }

    public static Method getMatching(Class<?> targetType, Method mToMatch) throws ReflectionException {
        Objects.requireNonNull(mToMatch);
        return lookup(targetType, mToMatch.getName(), mToMatch.getParameterTypes());
    }

    public static Method lookup(Class<?> targetType, String methodName, Class<?>... argTypes) throws ReflectionException {
        Objects.requireNonNull(targetType);
        Objects.requireNonNull(methodName);
        try {
            return targetType.getMethod(methodName, argTypes);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(
                    String.format("No such method: %s, for type: `%s`", e.getMessage(), targetType), e);
        }
    }

    public static Method lookup(Class<?>[] targetTypes, String methodName, Class<?>... argTypes) {
        Objects.requireNonNull(targetTypes);
        Objects.requireNonNull(methodName);
        for (Class<?> interfaceType : targetTypes) {
            try {
                return Methods.lookup(interfaceType, methodName, argTypes);
            } catch (ReflectionException ignored) {}
        }
        return null;
    }

    public static Object invoke(Object instance, Method method, Object... args) throws Throwable {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(method);
        try {
            return method.invoke(instance, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
