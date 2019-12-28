package com.dataspecks.builder;

import com.dataspecks.builder.exception.runtime.BuilderException;
import com.dataspecks.commons.function.UnsafeConsumer;

import java.util.ArrayList;
import java.util.List;

public class Configurator<T> {
    private final List<UnsafeConsumer<T>> configurators = new ArrayList<>();

    public void add(final UnsafeConsumer<T> c) {
        this.configurators.add(c);
    }

    public T configure(T instance) {
        for (UnsafeConsumer<T> c : configurators) {
            try {
                c.accept(instance);
            } catch (Throwable t) {
                String msg = instance == null
                        ? "Configuration failed."
                        : String.format("Failed to configure instance '%s'", instance.getClass());
                throw new BuilderException(msg, t);
            }
        }
        return instance;
    }
}
