package com.dataspecks.test;

public class DummyClass implements DummyInterface {
    @Override
    public Integer getA() {
        return 456;
    }

    @Override
    public Integer getB() {
        return 6666;
    }

    @Override
    public String foo(Integer integer, Integer a) {
        return String.valueOf(integer);
    }
}
