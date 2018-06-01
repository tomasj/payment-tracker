package cz.teejays.components;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Tracker maintains balance and exchange rate for each currency
 * - records with balance = 0 are not kept
 * - all exchange rates are kept
 */
@Log4j2
public class PaymentTracker {

    /**
     * Balances. Sum of all processed payment transactions in <key> ccy.
     */
    @Getter
    private HashMap<String, BigDecimal> balances = new HashMap<>();

    /**
     * Exchange rates. How many units of <key> currency can 1 unit of BASE ccy buy.
     */
    @Getter
    private HashMap<String, BigDecimal> exchangeRates = new HashMap<>();

    /**
     * Add payment to balance statement
     * @param ccy Target ccy
     * @param amount Amount to add (can be negative)
     */
    void processPayment(String ccy, BigDecimal amount){
        log.info("Tracking payment: " + ccy + " -> " + amount);

        // ccy present in map?
        if(balances.containsKey(ccy)){

            // add amount - compute new amount
            BigDecimal newAmount = balances.get(ccy).add(amount);

            // zero amount?
            if(BigDecimal.ZERO.equals(newAmount)){

                // remove ccy from balances
                balances.remove(ccy);

            } else {

                // set new amount to map
                balances.put(ccy, newAmount);
            }

        } else {

            // add any non-zero amounts to map
            if(!BigDecimal.ZERO.equals(amount)){
                balances.put(ccy, amount);
            }
        }
    }

    /**
     * Update exchange rate for given currency
     * - meaning 1 unit of BASE currency can buy {@code exchangeRate} units of {@code ccy} currency
     * @param ccy Target currency
     * @param exchangeRate New exchange rate
     */
    void updateExchangeRate(String ccy, BigDecimal exchangeRate){
        exchangeRates.put(ccy, exchangeRate);
    }

    BigDecimal getExchangeRate(String ccy){
        return exchangeRates.get(ccy);
    }
}
