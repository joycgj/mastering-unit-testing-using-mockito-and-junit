package com.packtpub.junit.recap;

import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Assumption {

    boolean isSonaRunning = false;

    @Test
    public void very_critical_test() {
        Assume.assumeFalse(isSonaRunning);
        assertTrue(true);
    }
}
