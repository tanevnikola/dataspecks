package com.dataspecks.test;

import java.util.Objects;

public class IncompleteDummyClass {


    public Integer getA() {
        return 1;
    }

    public Integer getB() {
        return null;
    }

    public Integer getC() {
        return null;
    }

    public String getBstring() {
        return null;
    }

    public String getCstring() {
        return null;
    }

   public void foo() {

    }

    public void testArgumentRewire(Integer i, Boolean b, String s) {

    }

    public void testArgumentRewire(Integer i, String s) {

    }

    public void testArgumentRewire(Boolean b, String s) {

    }

    public void testArgumentRewire(Integer i, Boolean b) {

    }

    public Integer testArgumentRewire(Boolean b, Integer i) {
        return null;
    }
}