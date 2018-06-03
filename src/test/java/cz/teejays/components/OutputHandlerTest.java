package cz.teejays.components;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused on testing currency amount format, printed to console.
 */
class OutputHandlerTest {

    @Test
    void printCurrencyToConsole() {

        // instance
        OutputHandler outputHandler = new OutputHandler(null);

        // var
        String out ;

        // basic
        out = outputHandler.formatCurrencyOutput("CZK", BigDecimal.valueOf(100), BigDecimal.valueOf(0.05));
        assertEquals(out, "CZK 100 (USD 5)");

        // decimal output
        out = outputHandler.formatCurrencyOutput("CZK", BigDecimal.valueOf(100.25), BigDecimal.valueOf(0.05));
        assertEquals(out, "CZK 100.25 (USD 5.01)");

        // decimal output - more precision in balance
        out = outputHandler.formatCurrencyOutput("CZK", BigDecimal.valueOf(133.333333), BigDecimal.valueOf(0.05));
        assertEquals(out, "CZK 133.33 (USD 6.67)");

        // decimal output - single decimal place
        out = outputHandler.formatCurrencyOutput("CZK", BigDecimal.valueOf(133.3), BigDecimal.valueOf(0.05));
        assertEquals(out, "CZK 133.3 (USD 6.67)");

        // decimal output - more precision in both balance and exchange rate
        out = outputHandler.formatCurrencyOutput("CZK", BigDecimal.valueOf(133.333333), BigDecimal.valueOf(0.0666666));
        assertEquals(out, "CZK 133.33 (USD 8.89)");
    }
}