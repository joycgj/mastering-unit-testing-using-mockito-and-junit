package com.packt.trading;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;

public class StockBrokerATest {

    @Mock
    MarketWatcher marketWatcher;

    @Mock
    Portfolio portfolio;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sanity() {
        assertNotNull(marketWatcher);
        assertNotNull(portfolio);
    }
}
