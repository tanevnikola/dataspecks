package com.dataspecks.test;

import com.dataspecks.builder.GenericBuilder;

import java.util.Objects;

public class ImmutableDummyClass {
    private int a = 7;
    private int b = 4;
    private int c = 0;

    private ImmutableDummyClass() {}

    public Integer getA() {
        return a;
    }

    public Integer getB() {
        return b;
    }

    public Integer getC() {
        return c;
    }

    public Integer foo(String value) {
        return 10000;
    }

    public void foo() {
    }


    public void testArgumentRewire(Integer i, Boolean b, Object[] s) {
        System.out.printf("i: '%s', b: '%s''\n", i, b);
    }

    public void testArgumentRewire(Integer i, Boolean b, String s) {
        System.out.printf("i: '%s', b: '%s', s: '%s'\n", i, b, s);
    }

    public void testArgumentRewire(String s) {
        System.out.printf("%s\n", s);
    }

    public String getBString() {
        return Objects.toString(b);
    }

    public String getCString() {
        return Objects.toString(c);
    }

    public static class Builder extends GenericBuilder<ImmutableDummyClass, ImmutableDummyClass> {
        Builder() {
            super(ImmutableDummyClass::new);
        }

        public Builder setA(int a) {
            configure(instance -> instance.a = a);
            return this;
        }

        public Builder setB(int b) {
            configure(instance -> instance.b = b);
            return this;
        }

        public Builder setC(int c) {
            configure(instance -> instance.c = c);
            return this;
        }
    }
}