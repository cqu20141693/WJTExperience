package com.wcc.exception.asserts;

import org.junit.Test;

public class AssertTest {


    @Test
    public void testAssert(){


        int age=10;
        checkAge(age);
        checkAge(101);
    }

    private void checkAge(int age) {
        assert age >=0&& age <100 : "age is invalid";
    }
}
