package cz.teejays.components;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTrackerConcurrentTest {

    private static final int PAYMENTS_TO_PROCESS = 100000;

    @Test
    void concurrentWritesAndReads(){

        // instance
        PaymentTracker paymentTracker = new PaymentTracker();
        Map<String, Integer> callCount = new HashMap<>();

        // prepare tasks
        // to run payments many times simultaneously
        Runnable addRunnable = () -> {
            int i;
            for (i = 0; i < PAYMENTS_TO_PROCESS; i++) {
                paymentTracker.processPayment("CZK", BigDecimal.valueOf(100));
            }
            callCount.put("+", i);

            // add 20 CZK, just to know we reach the end
            paymentTracker.processPayment("CZK", BigDecimal.valueOf(20));
        };
        Runnable subtractRunnable = () -> {
            int i = 0;
            for (i = 0; i < PAYMENTS_TO_PROCESS; i++) {
                paymentTracker.processPayment("CZK", BigDecimal.valueOf(-100));
            }
            callCount.put("-", i);

            // subtract 5 CZK, just to know we reach the end
            paymentTracker.processPayment("CZK", BigDecimal.valueOf(-5));
        };
        Runnable readRunnable = () -> {
            int i;
            for (i = 0; i < PAYMENTS_TO_PROCESS; i++) {
                if(i % 1000 == 0){
                    if(paymentTracker.getBalances().containsKey("CZK")){
                        System.out.println("Balance: " + paymentTracker.getBalances().get("CZK"));
                    }
                }
            }
            callCount.put("reads", i);
        };

        // starts threads for them
        Thread addThread = new Thread(addRunnable);
        Thread subtractThread = new Thread(subtractRunnable);
        Thread readThread = new Thread(readRunnable);

        // start threads
        addThread.start();
        subtractThread.start();
        readThread.start();

        // wait for complete
        try {
            addThread.join();
            subtractThread.join();
            readThread.join();
        } catch (Exception e){
            fail("Thread were interrupted when they shouldn't have been");
        }

        // check invoke count of methods
        assertEquals(callCount.get("+").intValue(), PAYMENTS_TO_PROCESS);
        assertEquals(callCount.get("-").intValue(), PAYMENTS_TO_PROCESS);
        assertEquals(callCount.get("reads").intValue(), PAYMENTS_TO_PROCESS);

        // check final balance
        assertEquals(BigDecimal.valueOf(15), paymentTracker.getBalances().get("CZK"));

    }

}