package com.dataspecks.commons.utils.reflection;

import com.dataspecks.commons.exception.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

public class Methods {
    public static Method lookup(Class<?> type, String name, Class<?>... args) throws ReflectionException {
        try {
            return type.getMethod(name, args);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(String.format("No such method: %s, for type: `%s`", e.getMessage(), type), e);
        }
    }

    public static Object invoke(Method m, Object obj, Object... args) throws Throwable {
        try {
            return m.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public static Method findMatching(Class<?> targetType, Method mToMatch) {
        try {
            return getMatching(targetType, mToMatch);
        } catch (ReflectionException e) {
            return null;
        }
    }

    public static Method getMatching(Class<?> targetType, Method mToMatch) throws ReflectionException {
        return lookup(targetType, mToMatch.getName(), mToMatch.getParameterTypes());
    }
}
