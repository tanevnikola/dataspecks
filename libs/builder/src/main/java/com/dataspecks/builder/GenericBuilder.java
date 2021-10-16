package com.dataspecks.builder;

import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.function.UnsafeConsumer;

import java.util.Objects;
import java.util.function.Supplier;

public class GenericBuilder<T, I extends T> implements Builder<T> {
    private final Supplier<I> instanceSupplier;
    private final Configurator<I> configurator = new Configurator<>();

    public GenericBuilder(Supplier<I> instanceSupplier) {
        DException.argue(Objects.nonNull(instanceSupplier));
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    public T build() {
        return validate(configurator.configure(instanceSupplier.get()));
    }

    protected void configure(UnsafeConsumer<I> configurator) {
        DException.argue(Objects.nonNull(configurator));
        this.configurator.add(configurator);
    }

    protected I validate(I t) {
        DException.argue(Objects.nonNull(t));
        return t;
    }
}
