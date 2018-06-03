package cz.teejays.components;

import cz.teejays.AppContext;
import cz.teejays.AppOptions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Can InputProcessor correctly interpret commands and take appropriate action?
 */
class InputProcessorTest {

    @Test
    void processInputLine() {

        // mock app context
        AppContext appContext = mock(AppContext.class);
        when(appContext.getOutputHandler()).thenReturn(mock(OutputHandler.class));
        when(appContext.getPaymentTracker()).thenReturn(mock(PaymentTracker.class));

        // instantiate
        InputProcessor inputProcessor = new InputProcessor(appContext);

        // payment cmd
        inputProcessor.processInputLine("czk 100", false);
        verify(appContext.getPaymentTracker()).processPayment(eq("CZK"), eq(BigDecimal.valueOf(100)));

        // exchange rate cmd
        inputProcessor.processInputLine("x usd/czk 20", false);
        verify(appContext.getPaymentTracker()).updateExchangeRate(eq("CZK"), eq(BigDecimal.valueOf(0.05d).setScale(10)));

    }
}