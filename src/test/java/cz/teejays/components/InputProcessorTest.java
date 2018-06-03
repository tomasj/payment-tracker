package cz.teejays.components;

import cz.teejays.AppContext;
import cz.teejays.AppOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Can InputProcessor correctly interpret commands and take appropriate action?
 */
class InputProcessorTest {

    AppContext appContext;
    InputProcessor inputProcessor;

    @BeforeEach
    void init(){

        // mock app context
        appContext = mock(AppContext.class);
        when(appContext.getOutputHandler()).thenReturn(mock(OutputHandler.class));
        when(appContext.getPaymentTracker()).thenReturn(mock(PaymentTracker.class));

        // instantiate
        inputProcessor = new InputProcessor(appContext);
    }

    @Test
    void processInputLine() {

        // payment cmd
        inputProcessor.processInputLine("czk 100", false);
        verify(appContext.getPaymentTracker()).processPayment(eq("CZK"), eq(BigDecimal.valueOf(100)));

        // exchange rate cmd
        inputProcessor.processInputLine("x usd/czk 20", false);
        verify(appContext.getPaymentTracker()).updateExchangeRate(eq("CZK"), eq(BigDecimal.valueOf(0.05d).setScale(10)));

    }

    @Test
    void processErrorInputLine() {

        // bad payment cmd
        inputProcessor.processInputLine("czks 100", false);
        verify(appContext.getOutputHandler()).handleInputError(eq("Payment processing error: " + "Currency symbol format error"), eq(false));

        // bad payment cmd from file
        inputProcessor.processInputLine("czk 100.000,00", true);
        verify(appContext.getOutputHandler()).handleInputError(eq("Payment processing error: " + "Unknown amount format"), eq(true));

        // bad exchange rate cmd
        inputProcessor.processInputLine("x usd-czk 20", false);
        verify(appContext.getOutputHandler()).handleInputError(eq("Exchange rate processing error: " + "Unknown input format - currencies"), eq(false));

    }
}