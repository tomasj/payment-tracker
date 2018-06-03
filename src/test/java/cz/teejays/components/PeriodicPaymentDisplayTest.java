package cz.teejays.components;

import cz.teejays.AppContext;
import cz.teejays.AppOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test the scheduler executes the job at start + more than once afterwards.
 */
class PeriodicPaymentDisplayTest {

    @Test
    void testScheduler() throws InterruptedException {

        // mock app context
        AppContext appContext = mock(AppContext.class);
        when(appContext.getPaymentTracker()).thenReturn(mock(PaymentTracker.class));

        // init and start teh scheduler
        PeriodicPaymentDisplay periodicPaymentDisplay = new PeriodicPaymentDisplay(appContext);
        periodicPaymentDisplay.startPeriodicDisplaying();

        // ok now how do we know it works?
        // the scheduler -> calls the job -> job calls paymentTracker (and that's mock, so we will know about the call)

        // check it trigger short while after start
        Thread.sleep(1 * 1000);
        verify(appContext.getPaymentTracker()).getBalances(); // 1 invocation

        // wait for 2 displays
        Thread.sleep((AppOptions.DISPLAY_INTERVAL + 1) * 1000); // wait for periodic display
        Thread.sleep((AppOptions.DISPLAY_INTERVAL + 1) * 1000); // wait for periodic display

        // check number of getBalances invocations
        verify(appContext.getPaymentTracker(), times(3)).getBalances(); // 3 invocation in total

        // close
        periodicPaymentDisplay.stopPeriodicDisplaying();
    }


}