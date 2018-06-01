package cz.teejays.components;

import cz.teejays.AppContext;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

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
