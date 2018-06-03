package cz.teejays.components;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test ability to keep correct balance, based on payments.
 */
class PaymentTrackerTest {

    @Test
    void processPayment() {

        PaymentTracker paymentTracker = new PaymentTracker();

        // basic 1.
        paymentTracker.processPayment("CZK", BigDecimal.valueOf(100));
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertEquals(BigDecimal.valueOf(100), paymentTracker.getBalances().get("CZK"));

        // basic 2. - add
        paymentTracker.processPayment("CZK", BigDecimal.valueOf(20));
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertEquals(BigDecimal.valueOf(120), paymentTracker.getBalances().get("CZK"));

        // basic 3. - subtract
        paymentTracker.processPayment("CZK", BigDecimal.valueOf(-40));
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertEquals(BigDecimal.valueOf(80), paymentTracker.getBalances().get("CZK"));

        // decimal - basic precision
        paymentTracker.processPayment("CZK", BigDecimal.valueOf(0.55d));
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertEquals(BigDecimal.valueOf(80.55d), paymentTracker.getBalances().get("CZK"));

        // decimal - more precision
        paymentTracker.processPayment("CZK", BigDecimal.valueOf(0.4499999d));
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertEquals(BigDecimal.valueOf(80.9999999d), paymentTracker.getBalances().get("CZK"));

        // one more currency
        paymentTracker.processPayment("USD", BigDecimal.valueOf(1000));
        assertEquals(2, paymentTracker.getBalances().size());
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertTrue(paymentTracker.getBalances().containsKey("USD"));
        assertEquals(BigDecimal.valueOf(1000), paymentTracker.getBalances().get("USD"));

        // ccy removed when balance = 0
        paymentTracker.processPayment("USD", BigDecimal.valueOf(-1000));
        assertEquals(1, paymentTracker.getBalances().size());
        assertTrue(paymentTracker.getBalances().containsKey("CZK"));
        assertFalse(paymentTracker.getBalances().containsKey("USD"));
    }
}