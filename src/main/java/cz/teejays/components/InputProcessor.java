package cz.teejays.components;

import cz.teejays.AppContext;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputProcessor {

    private AppContext appContext;

    private boolean running = false;
    Scanner scanner;

    public InputProcessor(AppContext appContext) {
        this.appContext = appContext;
        scanner = new Scanner(System.in);
    }

    public void startReading(){
        running = true;
        while(running) {
            String inputLine = scanner.nextLine();
            processInputLine(inputLine, false);
        }
    }

    public void processInputLine(String inputLine, boolean fileInput){

        // what we got? payment or exchange rate?
        if(InputParserHelper.isExchangeRate(inputLine)){

            // exchange
            Pair<String, BigDecimal> exchangeRate;
            try {
                exchangeRate = InputParserHelper.parseExchangeRateInputLine(inputLine);
            } catch (IllegalArgumentException e){
                appContext.getOutputHandler().handleInputError("Exchange rate processing error: " + e.getMessage(), fileInput);
                return;
            }

            // process exchange rate update
            appContext.getPaymentTracker().updateExchangeRate(exchangeRate.getKey(), exchangeRate.getValue());


        } else {

            // payment
            Pair<String, BigDecimal> payment;
            try {
                 payment = InputParserHelper.parsePaymentInputLine(inputLine);
            } catch (IllegalArgumentException e){
                appContext.getOutputHandler().handleInputError("Payment processing error: " + e.getMessage(), fileInput);
                return;
            }

            // process payment
            appContext.getPaymentTracker().processPayment(payment.getKey(), payment.getValue());
        }
    }

    public void stopReading(){
        scanner.close();
        running = false;
    }

    public boolean isReading(){
        return running;
    }
}
