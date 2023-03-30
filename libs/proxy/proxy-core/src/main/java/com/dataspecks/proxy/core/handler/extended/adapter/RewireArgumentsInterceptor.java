package com.dataspecks.proxy.core.handler.extended.adapter;

import com.dataspecks.proxy.core.handler.base.interceptor.ArgumentsInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RewireArgumentsInterceptor implements ArgumentsInterceptor {
    private final Map<Integer, RewireOperation> rewireMap = new HashMap<>();

    public Map<Integer, RewireOperation> getRewireMap() {
        return rewireMap;
    }

    @Override
    public Object[] intercept(Object proxy, Method method, Object... args) throws Throwable {
        List<Object> result = new ArrayList<>();
        for (Integer key : rewireMap.keySet()) {
            result.add(rewireMap.get(key).rewire(args));
        }
        return result.toArray();
    }
}
