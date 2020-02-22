package com.packtpub.junit.recap;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class AssertTest {

    @Test
    public void assertTrueAndFalseTest() {
        Assert.assertTrue(true);
        Assert.assertFalse(false);
    }

    @Test
    public void assertNullAndNotNullTest() {
        Object myObject = null;
        Assert.assertNull(myObject);

        myObject = new String("Some value");
        Assert.assertNotNull(myObject);
    }

    @Test
    public void assertEqualsTest() {
        Integer i = new Integer("5");
        Integer j = new Integer("5");
        assertEquals(i, j);
    }

    @Test
    public void assertNotSameTest() {
        Integer i = new Integer("5");
        Integer j = new Integer("5");
        assertNotSame(i, j);
    }

    @Test
    public void assertSameTest() {
        Integer i = new Integer("5");
        Integer j = i;
        assertSame(i, j);
    }

    // Giving a proper description explains the intention behind ignoring the test.
    @Test
    @Ignore("John's holiday stuff failing")
    public void when_today_is_holiday_then_stop_alarm() {
    }
}
