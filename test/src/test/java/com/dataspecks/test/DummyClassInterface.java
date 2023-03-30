package com.dataspecks.test;

public interface DummyClassInterface {
    Integer getA();

    Integer getB();

    Integer getC();

    String getBstring();

    String getCstring();

    String foo(Integer integer);

    void foo();

    void testArgumentRewire(Integer i, Boolean b, String s);
    void testArgumentRewire(Integer i, String s);
    void testArgumentRewire(Boolean b, String s);
    void testArgumentRewire(Integer i, Boolean b);
    Integer testArgumentRewire(Boolean b, Integer i);

}
