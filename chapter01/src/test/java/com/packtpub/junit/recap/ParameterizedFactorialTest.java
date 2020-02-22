package com.packtpub.junit.recap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterizedFactorialTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, 1}, {1, 1}, {2, 2}, {3, 6}, {4, 24}, {5, 120}, {6, 720}
        });
    }

    private int number;
    private int expectedResult;

    public ParameterizedFactorialTest(int number, int expectedResult) {
        this.number = number;
        this.expectedResult = expectedResult;
    }

    @Test
    public void factorial() {
        Factorial fact = new Factorial();
        assertEquals(fact.factorial(number), expectedResult);
    }
}
