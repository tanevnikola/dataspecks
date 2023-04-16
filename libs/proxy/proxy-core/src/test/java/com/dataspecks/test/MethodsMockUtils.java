package com.dataspecks.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MethodsMockUtils {
    private final Map<MethodCall, Object> methodCallResults = new ConcurrentHashMap<>();

    public static record MethodCall(Object instance, Method method, Object... args) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodCall that = (MethodCall) o;
            return Objects.equals(instance, that.instance) && Objects.equals(method, that.method) && Arrays.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(instance, method);
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }
}
