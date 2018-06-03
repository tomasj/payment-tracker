package cz.teejays.components;

import cz.teejays.AppOptions;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Can InputParserHelper parse input command in correct way?
 * Note: command line is always passed after upper case conversion.
 */
class InputParserHelperTest {

    @Test
    void parsePaymentInputLine() {

        // reusable vars
        Pair<String, BigDecimal> out;
        Throwable exception;

        // basic
        out = InputParserHelper.parsePaymentInputLine("CZK 100");
        assertEquals("CZK", out.getKey());
        assertEquals(BigDecimal.valueOf(100), out.getValue());

        // decimals
        out = InputParserHelper.parsePaymentInputLine("GBP 11.11");
        assertEquals("GBP", out.getKey());
        assertEquals(BigDecimal.valueOf(11.11d).setScale(2), out.getValue());
        out = InputParserHelper.parsePaymentInputLine("CHF 215,50");
        assertEquals("CHF", out.getKey());
        assertEquals(BigDecimal.valueOf(215.50d).setScale(2), out.getValue());

        // bad format
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parsePaymentInputLine("JPY 100 000");
        });
        assertEquals("Unknown input format", exception.getMessage());

        // ccy format error
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parsePaymentInputLine("USDS 1000");
        });
        assertEquals("Currency symbol format error", exception.getMessage());

        // amount format error - US currency format
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parsePaymentInputLine("CZK 100,--");
        });
        assertEquals("Unknown amount format", exception.getMessage());

        // amount format error - US currency format
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parsePaymentInputLine("USD 100,000,000.00");
        });
        assertEquals("Unknown amount format", exception.getMessage());
    }

    @Test
    void parseExchangeRateInputLine() {

        // reusable vars
        Pair<String, BigDecimal> out;
        Throwable exception;

        // basic - order same as stored
        out = InputParserHelper.parseExchangeRateInputLine("X CZK/USD 0.05");
        assertEquals("CZK", out.getKey());
        assertEquals(BigDecimal.valueOf(0.05d).setScale(2), out.getValue());

        // order inverted
        out = InputParserHelper.parseExchangeRateInputLine("X USD/CZK 20");
        assertEquals("CZK", out.getKey());
        assertEquals(BigDecimal.valueOf(0.05d).setScale(10), out.getValue());

        // "," as decimal separator
        out = InputParserHelper.parseExchangeRateInputLine("X CZK/USD 0,05");
        assertEquals("CZK", out.getKey());
        assertEquals(BigDecimal.valueOf(0.05d).setScale(2), out.getValue());

        // lower case
        out = InputParserHelper.parseExchangeRateInputLine("X CZK/USD 0.05");
        assertEquals("CZK", out.getKey());
        assertEquals(BigDecimal.valueOf(0.05d).setScale(2), out.getValue());

        // bad format - mistake in exchange indicator
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parseExchangeRateInputLine("Y CZK/USD 0.05");
        });
        assertEquals("Unknown input format", exception.getMessage());

        // bad format - too many spaces
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parseExchangeRateInputLine("X CZK USD 0.05");
        });
        assertEquals("Unknown input format", exception.getMessage());

        // bad ccy separator
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parseExchangeRateInputLine("X CZK-USD 0.05");
        });
        assertEquals("Unknown input format - currencies", exception.getMessage());

        // no BASE ccy
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parseExchangeRateInputLine("X GBP/EUR 1.22");
        });
        assertEquals(AppOptions.BASE_CURRENCY + " must be present on one side of conversion pair",
                exception.getMessage());

        // BASE ccy on both sides!
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parseExchangeRateInputLine("X USD/USD 1.00");
        });
        assertEquals(AppOptions.BASE_CURRENCY + " must be present on one side of conversion pair, but not on both :)", exception.getMessage());

        // bad format of the rate
        exception = assertThrows(IllegalArgumentException.class, () -> {
            InputParserHelper.parseExchangeRateInputLine("X USD/JPY 1.000,25");
        });
        assertEquals("Unknown amount format", exception.getMessage());
    }

    @Test
    void isExchangeRate() {
        assertTrue(InputParserHelper.isExchangeRate("X USD/CZK 22.30"));
        assertFalse(InputParserHelper.isExchangeRate("CZK 100"));
    }
}