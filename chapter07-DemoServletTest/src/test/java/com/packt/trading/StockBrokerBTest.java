package com.packt.trading;

import com.packt.trading.dto.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StockBrokerBTest {

    @Mock
    MarketWatcher marketWatcher;

    @Mock
    Portfolio portfolio;

    StockBroker broker;

    Stock globalStock =  when(mock(Stock.class).getPrice()).thenReturn(BigDecimal.ONE).getMock();

    @Before
    public void setUp() {
        broker = new StockBroker(marketWatcher);
    }

    @Test
    public void sanity() {
        assertNotNull(marketWatcher);
        assertNotNull(portfolio);
    }

    @Test
    public void marketWatcher_Returns_current_stock_status() {
        Stock uvsityCorp = new Stock("UV", "UVSITY Corporation ", new BigDecimal(100.00));
        when(marketWatcher.getQuote(anyString())).thenReturn(uvsityCorp);
        assertNotNull(marketWatcher.getQuote("UV"));
    }

    @Test
    public void when_ten_percent_gain_then_the_stock_is_sold() {
        when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(new BigDecimal("10.00"));
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
        when(marketWatcher.getQuote(anyString())).thenReturn(aCorp);
        broker.perform(portfolio, aCorp);
        verify(portfolio).sell(aCorp, 10);
    }

    @Test
    public void verify_zero_interaction() {
        verifyZeroInteractions(marketWatcher, portfolio);
    }

    @Test
    public void verify_no_more_interaction() {
        Stock noStock = null;
        portfolio.getAvgPrice(noStock);
        portfolio.sell(null, 0);
        verify(portfolio).getAvgPrice(eq(noStock));
        // this will fail as the sell method was invoked
        verifyNoMoreInteractions(portfolio);
    }

    @Test
    public void argument_matcher() {
        when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(new BigDecimal("10.00"));
        Stock blueChipStock = new Stock("FB", "FB Corp", new BigDecimal(1000.00));
        Stock otherStock = new Stock("XY", "XY Corp", new BigDecimal(5.00));
        when(marketWatcher.getQuote(argThat(new BlueChipStockMatcher()))).thenReturn(blueChipStock);
        when(marketWatcher.getQuote(argThat(new OtherStockMatcher()))).thenReturn(otherStock);

        broker.perform(portfolio, blueChipStock);
        verify(portfolio).sell(blueChipStock, 10);

        broker.perform(portfolio, otherStock);
        verify(portfolio, new Times(0)).sell(otherStock, 10);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsException() {
        when(portfolio.getAvgPrice(isA(Stock.class))).thenThrow(new IllegalStateException("Database down"));

        portfolio.getAvgPrice(new Stock(null, null, null));
//        fail("Should throw exception");
    }

    @Test(expected = IllegalStateException.class)
    public void throwsException_void_methods() {
        doThrow(new IllegalStateException()).when(portfolio).buy(isA(Stock.class));

        portfolio.buy(new Stock(null, null, null));
    }

    @Test
    public void consecutive_calls() {
        Stock stock = new Stock(null, null, null);
        when(portfolio.getAvgPrice(stock)).thenReturn(BigDecimal.TEN, BigDecimal.ZERO);
        assertEquals(BigDecimal.TEN, portfolio.getAvgPrice(stock));
        assertEquals(BigDecimal.ZERO, portfolio.getAvgPrice(stock));
        assertEquals(BigDecimal.ZERO, portfolio.getAvgPrice(stock));
    }

    Map<String, List<Stock>> stockMap = new HashMap<>();

    @Test
    public void answering() {
        stockMap.clear();
        doAnswer(new BuyAnswer()).when(portfolio).buy(isA(Stock.class));
        when(portfolio.getCurrentValue()).then(new TotalPriceAnswer());

        portfolio.buy(new Stock("A", "A", BigDecimal.TEN));
        portfolio.buy(new Stock("B", "B", BigDecimal.ONE));

        assertEquals(new BigDecimal("11"), portfolio.getCurrentValue());
    }

    @Test
    public void spying() {
        Stock realStock = new Stock("A", "Company A", BigDecimal.ONE);
        Stock spyStock = spy(realStock);

        assertEquals("A", spyStock.getSymbol());

        spyStock.updatePrice(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, spyStock.getPrice());

        when(spyStock.getPrice()).thenReturn(BigDecimal.TEN);

        spyStock.updatePrice(new BigDecimal("7"));
        assertNotEquals(new BigDecimal("7"), spyStock.getPrice());
        assertEquals(BigDecimal.TEN, spyStock.getPrice());
    }

    // This test will fail
    @Test
    public void doReturn_is_not_type_safe() {
        // then return is type safe- It has to return a BigDecimal
        when(portfolio.getCurrentValue()).thenReturn(BigDecimal.ONE);

        // method call works fine
        portfolio.getCurrentValue();
        // returning a String instead of BigDecimal

        doReturn("See returning a String").when(portfolio.getCurrentValue());
        // this call will fail
        portfolio.getCurrentValue();
    }

    @Test
    public void doReturn_usage_0() {
        List<String> list = new ArrayList<>();
        List<String> spy = spy(list);

        when(spy.get(0)).thenReturn("now reachable");
        assertEquals("now reachable", spy.get(0));
    }

    @Test
    public void doReturn_usage() {
        List<String> list = new ArrayList<>();
        List<String> spy = spy(list);

        // doReturn fixed the issue
        doReturn("now reachable").when(spy).get(0);
        assertEquals("now reachable", spy.get(0));
    }

    @Test
    public void argument_captor() {
        when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(new BigDecimal("10.00"));
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
        when(marketWatcher.getQuote(anyString())).thenReturn(aCorp);
        broker.perform(portfolio, aCorp);

        ArgumentCaptor<String> stockIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(marketWatcher).getQuote(stockIdCaptor.capture());
        assertEquals("A", stockIdCaptor.getValue());

        ArgumentCaptor<Stock> stockCaptor = ArgumentCaptor.forClass(Stock.class);
        ArgumentCaptor<Integer> stockSellCountCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(portfolio).sell(stockCaptor.capture(), stockSellCountCaptor.capture());
        assertEquals("A", stockCaptor.getValue().getSymbol());
        assertEquals(10, stockSellCountCaptor.getValue().intValue());
    }

    @Test
    public void inorder() {
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));

        portfolio.getAvgPrice(aCorp);
        portfolio.getCurrentValue();
        marketWatcher.getQuote("X");
        portfolio.buy(aCorp);

        InOrder inOrder = inOrder(portfolio, marketWatcher);
        inOrder.verify(portfolio).getAvgPrice(isA(Stock.class));
        inOrder.verify(portfolio).getCurrentValue();
        inOrder.verify(marketWatcher).getQuote(anyString());
        inOrder.verify(portfolio).buy(isA(Stock.class));
    }

    @Test
    public void changing_default() {
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
        Portfolio pf = mock(Portfolio.class);
        // default null is returned
        assertNull(pf.getAvgPrice(aCorp));

        Portfolio pf1 = mock(Portfolio.class, RETURNS_SMART_NULLS);
        // a smart null is returned
        System.out.println("#1 " + pf1.getAvgPrice(aCorp));
        assertNotNull(pf1.getAvgPrice(aCorp));

        Portfolio pf2 = mock(Portfolio.class, RETURNS_MOCKS);
        // a mock is returned
        System.out.println("#2 " + pf2.getAvgPrice(aCorp));
        assertNotNull(pf2.getAvgPrice(aCorp));

        Portfolio pf3 = mock(Portfolio.class, RETURNS_DEEP_STUBS);
        // a deep stubbed mock is returned
        System.out.println("#3 " + pf3.getAvgPrice(aCorp));
        assertNotNull(pf3.getAvgPrice(aCorp));
    }

    @Test
    public void resetMock() {
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));

        Portfolio portfolio = mock(Portfolio.class);
        when(portfolio.getAvgPrice(eq(aCorp))).thenReturn(BigDecimal.ONE);
        assertNotNull(portfolio.getAvgPrice(aCorp));

        reset(portfolio);
        // Resets the stub, so getAvgPrice returns NULL
        assertNull(portfolio.getAvgPrice(aCorp));
    }

    @Test
    public void access_global_mock() {
        assertEquals(BigDecimal.ONE, globalStock.getPrice());
    }

    @Test
    public void mocking_details() {
        Portfolio pf1 = mock(Portfolio.class, RETURNS_MOCKS);
        BigDecimal result = pf1.getAvgPrice(globalStock);
        assertNotNull(result);
        assertTrue(mockingDetails(pf1).isMock());

        Stock myStock = new Stock(null, null, null);
        Stock spy = spy(myStock);
        assertTrue(mockingDetails(spy).isSpy());
    }

    class BuyAnswer implements Answer<Object> {
        @Override
        public Object answer(InvocationOnMock invocation) {
            Stock newStock = (Stock) invocation.getArguments()[0];
            List<Stock> stocks = stockMap.get(newStock.getSymbol());
            if (stocks != null) {
                stocks.add(newStock);
            } else {
                stocks = new ArrayList<>();
                stocks.add(newStock);
                stockMap.put(newStock.getSymbol(), stocks);
            }
            return null;
        }
    }

    class TotalPriceAnswer implements Answer<BigDecimal> {
        @Override
        public BigDecimal answer(InvocationOnMock invocation) {
            BigDecimal avgPrice = BigDecimal.ZERO;
            for (String stockId : stockMap.keySet()) {
                for (Stock stock : stockMap.get(stockId)) {
                    avgPrice = avgPrice.add(stock.getPrice());
                }
            }
            return avgPrice;
        }
    }

    class BlueChipStockMatcher extends ArgumentMatcher<String> {
        @Override
        public boolean matches(Object symbol) {
            return "FB".equals(symbol) || "APPL".equals(symbol);
        }
    }

    class OtherStockMatcher extends BlueChipStockMatcher {
        @Override
        public boolean matches(Object symbol) {
            return !super.matches(symbol);
        }
    }
}
