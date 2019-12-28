package com.dataspecks.commons.reflection;

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
            throw new ReflectionException(String.format("No such method: %s", e.getMessage()), e);
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
        Class<?>[] mToMatchParams = mToMatch.getParameterTypes();
        return Arrays.stream(targetType.getMethods())
                .filter(method -> method.getName().equals(mToMatch.getName()))
                .filter(method -> method.getParameterTypes().length == mToMatchParams.length)
                .filter(method -> {
                    Class<?>[] methodCandidateParams = method.getParameterTypes();
                    return IntStream.range(0, methodCandidateParams.length)
                            .allMatch(i -> methodCandidateParams[i].equals(mToMatchParams[i]));
                })
                .findFirst().orElse(null);
    }

    public static Method getMatching(Class<?> targetType, Method mToMatch) throws ReflectionException {
        return Optional.ofNullable(findMatching(targetType, mToMatch))
                .orElseThrow(() -> new ReflectionException("No matching method found in the target instance"));
    }
}
