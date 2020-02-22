package com.packtpub.junit.recap;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TimeOutTest {

    @Test(timeout = 10)
    public void forEver() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }
}
