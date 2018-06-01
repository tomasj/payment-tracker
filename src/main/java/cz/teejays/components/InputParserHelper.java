package cz.teejays.components;

import cz.teejays.AppOptions;
import javafx.util.Pair;

import java.math.BigDecimal;

public class InputParserHelper {

    public static Pair<String, BigDecimal> parsePaymentInputLine(String inputLine) throws IllegalArgumentException {
        String[] split = inputLine.split(" ");

        // should have 2 part currency and amount
        if(split.length != 2){ throw new IllegalArgumentException("Unknown input format"); }

        // currency symbol should be 3 chars long
        if(split[0].length() != 3){ throw new IllegalArgumentException("Currency symbol format error"); }

        //
        String currency = split[0].toUpperCase();

        // parse amount
        BigDecimal amount;
        try {
            amount = new BigDecimal(split[1]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Unknown amount format");
        }

        //
        return new Pair<>(currency, amount);
    }

    /**
     * Parse line to new exchange rate to BASE currency (defaults to USD)
     * eg.: X USD/CZK 22.22 means one dollar buys 22.22 CZKs ... (gets inverted to X CZK/USD 0.045)
     * eg.: X CZK/USD 0.045 means 1 CZK buy you 4.5 cents
     * @param inputLine String input
     * @return Pair of currency code and amount / exchange rate
     * @throws IllegalArgumentException When illagal format of input is observed
     */
    public static Pair<String, BigDecimal> parseExchangeRateInputLine(String inputLine) throws IllegalArgumentException {

        // strip X switch first
        inputLine = inputLine.replaceFirst("X ", "");

        // split to ccy/ccy and amount
        String[] split = inputLine.split(" ");

        // should have 2 part currency and amount
        if(split.length != 2){ throw new IllegalArgumentException("Unknown input format"); }

        // further split ccy/ccy
        String[] currencies = split[0].split("/");
        if(currencies.length != 2){ throw new IllegalArgumentException("Unknown input format - currencies"); }

        // currency symbol should be 3 chars long
        if(currencies[0].length() != 3 || currencies[1].length() != 3){ throw new IllegalArgumentException("Currency symbol format error"); }

        // convert exchange currencies to UC
        String currencyMain = currencies[0].toUpperCase();
        String currencyTarget = currencies[1].toUpperCase();

        // at least one ccy must be our BASE
        if(!AppOptions.BASE_CURRENCY.equals(currencyMain) && !AppOptions.BASE_CURRENCY.equals(currencyTarget)){
            throw new IllegalArgumentException(AppOptions.BASE_CURRENCY + " must be present on one side of conversion pair");
        }

        // BASE/BASE
        if(AppOptions.BASE_CURRENCY.equals(currencyMain) && AppOptions.BASE_CURRENCY.equals(currencyTarget)){
            throw new IllegalArgumentException(AppOptions.BASE_CURRENCY + " must be present on one side of conversion pair, but not on both :)");
        }

        // parse exchange rate
        BigDecimal exchangeRate;
        try {
            exchangeRate = new BigDecimal(split[1]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Unknown amount format");
        }

        // if currencyTarget is not BASE ccy, we have to invert the exchange rate
        // - we need to know how many units of BASE ccy can 1 unit of <currencyTarget> buy
        if(AppOptions.BASE_CURRENCY.equals(currencyMain)){
            exchangeRate = BigDecimal.ONE.divide(exchangeRate, 10, BigDecimal.ROUND_HALF_UP);

            // switch main and secondary ccy
            currencyMain = currencyTarget;
            currencyTarget = AppOptions.BASE_CURRENCY;
        }

        //
        return new Pair<>(currencyMain, exchangeRate);
    }

    /**
     * Does input line contain updated exchange rate?
     * @param inputLine Text input command
     * @return True - line updates exchange rate, False - line represents a payment
     */
    public static boolean isExchangeRate(String inputLine){
        return inputLine.startsWith("X ");
    }

}
