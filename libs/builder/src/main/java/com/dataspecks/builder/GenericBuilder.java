package com.dataspecks.builder;

import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.function.UnsafeConsumer;

import java.util.Objects;
import java.util.function.Supplier;

public class GenericBuilder<T> implements Builder<T> {
    private final Supplier<T> instanceSupplier;
    private final Configurator<T> configurator = new Configurator<>();

    public GenericBuilder(Supplier<T> instanceSupplier) {
        DException.argue(Objects.nonNull(instanceSupplier));
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    public T build() {
        return validate(configurator.configure(instanceSupplier.get()));
    }

    protected void configure(UnsafeConsumer<T> configurator) {
        DException.argue(Objects.nonNull(configurator));
        this.configurator.add(configurator);
    }

    protected T validate(T t) {
        DException.argue(Objects.nonNull(t));
        return t;
    }
}
