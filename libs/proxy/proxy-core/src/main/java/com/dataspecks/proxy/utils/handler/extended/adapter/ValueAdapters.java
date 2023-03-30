package com.dataspecks.proxy.utils.handler.extended.adapter;

import com.dataspecks.commons.exception.unchecked.DsUncheckedException;
import com.dataspecks.proxy.core.exception.unchecked.DeadEndException;
import com.dataspecks.proxy.core.handler.extended.adapter.ValueAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public final class ValueAdapters {
    private ValueAdapters() {}


    /**
     * A pass-through {@link ValueAdapter}. It returns the same value passed to the adapter.
     */
    public final static ValueAdapter<Object> PassThrough = arg -> arg;

    /**
     * Build a trivial {@link ValueAdapter} that simply returns the provided argument
     *
     * @param val this value will be the result of the call
     * @return same as the parameter val
     */
    public static  ValueAdapter<Object> trivial(Object val) {
        return _unused -> val;
    }

    /**
     * A {@link ValueAdapter} that calls the {@link Objects#toString(Object)} for the provided value
     */
    public final static ValueAdapter<Object> ToString = Objects::toString;

    /**
     * A Dead-End {@link ValueAdapter} that raises a {@link DeadEndException}
     */
    public final static ValueAdapter<Object> DeadEnd = arg -> {
        throw new DeadEndException("Dead End :( value adapter");
    };

    /**
     * A {@link ValueAdapter} that converts String to Integer.
     */
    public final static ValueAdapter<Object> StringToInteger = arg -> {
        DsUncheckedException.argue(arg instanceof String,
                String.format("Expected type '%s', got '%s'", String.class, Optional.ofNullable(arg)
                        .map(o -> o.getClass().toString())
                        .orElse("null")));
        return Integer.parseInt((String)arg);
    };

    /**
     * A {@link ValueAdapter} that casts the input argument to the provided type.
     *
     * @param type type to cast to
     * @param <T> type to cast to
     * @return casted value
     */
    public static <T> ValueAdapter<Object> castTo(Class<T> type) {
        return type::cast;
    }

    /**
     * A {@link ValueAdapter} that serializes a single value to ByteArray.
     */
    public final static ValueAdapter<Object> Serialize = arg -> {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(arg);
        oos.flush();
        return bos.toByteArray();
    };

    /**
     * A {@link ValueAdapter} that deserializes a single value from ByteArray to an Object, it doesnt care what type
     * the object is.
     */
    public final static ValueAdapter<Object> Deserialize = arg -> {
        DsUncheckedException.argue(arg instanceof byte[]);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream((byte[])arg));
        return in.readObject();
    };

    /**
     * Build a multiple-value {@link ValueAdapter} using a single-value {@link ValueAdapter} for each of the provided
     * values
     *
     * @param adapter a single-value {@link ValueAdapter}
     * @return multiple-value {@link ValueAdapter}
     */
    public static ValueAdapter<Object[]> using(ValueAdapter<Object> adapter) {
        return adapter::adapt;
    }

    public final static ValueAdapter<Object[]> SerializeArray = using(Serialize);

    public final static ValueAdapter<Object[]> DeserializeArray = using(Deserialize);

    /**
     * A pass-through multiple-value {@link ValueAdapter}
     */
    public final static ValueAdapter<Object[]> PassThroughArray = using(PassThrough);

    /**
     * A multiple-value {@link ValueAdapter} that concatenates the provided arguments.
     */
    public final static ValueAdapter<Object[]> Concatenate = concatenate("");

    /**
     * Build a multiple-value {@link ValueAdapter} that concatenates the provided arguments.
     *
     * @param delimiter the delimiter to be used for the concatenation
     * @return a multiple-value {@link ValueAdapter}
     */
    public static ValueAdapter<Object[]> concatenate(String delimiter) {
        return concatenate("", "", delimiter);
    }

    /**
     * Build a multiple-value {@link ValueAdapter} that concatenates the provided arguments.
     *
     * @param delimiter the delimiter to be used for the concatenation
     * @param prefix the prefix to be used for the concatenation
     * @param suffix the suffix to be used for the concatenation
     * @return a multiple-value {@link ValueAdapter}
     */
    public static ValueAdapter<Object[]> concatenate(String delimiter, String prefix, String suffix) {
        StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);
        return val -> {
            Arrays.stream(val)
                    .map(Object::toString)
                    .forEach(stringJoiner::add);
            return stringJoiner.toString();
        };
    }
}
