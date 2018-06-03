package cz.teejays.components;

import cz.teejays.AppContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Prints balance for each currency, along with it's exchange rate (if present).
 */
public class PeriodicPaymentDisplayJob implements Job {

    public static final String APP_CONTEXT_QUARTZ_KEY = "appContext";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // read app context
        AppContext appContext = (AppContext) jobExecutionContext.getJobDetail().getJobDataMap().get(APP_CONTEXT_QUARTZ_KEY);

        // print balances
        for (Map.Entry<String, BigDecimal> balanceEntry : appContext.getPaymentTracker().getBalances().entrySet()) {

            // get exchange rate
            BigDecimal exchangeRate = appContext.getPaymentTracker().getExchangeRates().get(balanceEntry.getKey());

            // output to console
            appContext.getOutputHandler().printCurrencyToConsole(balanceEntry.getKey(), balanceEntry.getValue(), exchangeRate);
        }
    }
}
