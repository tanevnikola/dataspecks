package com.dataspecks.commons.utils.reflection;

import com.dataspecks.commons.core.exception.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Methods {
    /**
     * Tries to find a matching method in the target type.
     *
     * @param targetType {@link Class} within which to search
     * @param mToMatch the {@link Method} to match
     * @return a matching {@link Method} or null if none found
     */
    public static Method findMatching(Class<?> targetType, Method mToMatch) {
        try {
            return getMatching(targetType, mToMatch);
        } catch (ReflectionException e) {
            return null;
        }
    }

    /**
     * Searches for an instance whose type declares a method that matches the provided method.
     * <p/>
     * Exact match takes precedence over <i>loose</i> match
     * (<i>by name and arguments only without considering the declaring type</i>).
     *
     * @param targetInstances a {@link Collection} of instances within which to search
     * @param mToMatch the {@link Method} to match
     * @return matched instance (loose or exact) or null if none found
     */
    public static Object findMatchingInstance(Collection<Object> targetInstances, Method mToMatch) {
        Object instanceCandidate = null;
        for (Object instance : targetInstances) {
            Method methodCandidate = Methods.findMatching(instance.getClass(), mToMatch);
            if (methodCandidate != null) {
                if (Objects.isNull(instanceCandidate)) {
                    instanceCandidate = instance;
                }
                if (methodCandidate.equals(mToMatch)) {
                    return instance;
                }
            }
        }
        return instanceCandidate;
    }


    /**
     * Retrieve a matching method.
     *
     * @param targetType {@link Class} within which to search
     * @param mToMatch the {@link Method} to match
     * @return a matching {@link Method} or null if none found
     * @throws ReflectionException if no matching method can be found
     */
    public static Method getMatching(Class<?> targetType, Method mToMatch) throws ReflectionException {
        Objects.requireNonNull(mToMatch);
        return lookup(targetType, mToMatch.getName(), mToMatch.getParameterTypes());
    }

    /**
     *
     * @param targetType
     * @param methodName
     * @param argTypes
     * @return
     * @throws ReflectionException
     */
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
