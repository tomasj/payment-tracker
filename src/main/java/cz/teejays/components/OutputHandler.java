package cz.teejays.components;

import cz.teejays.AppContext;
import cz.teejays.AppOptions;

import java.math.BigDecimal;

/**
 * Error outputs are handled here.
 */
public class OutputHandler {

    private AppContext appContext;

    public OutputHandler(AppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Cleans up app context, write message to console and exit app
     * @param message Goodbye message
     * @param returnCode Return code
     */
    public void exitApp(String message, int returnCode) {

        // normal or error output?
        if(returnCode == 0){
            System.out.println(message);
        }else{
            System.err.println(message);
        }

        // exit
        appContext.exit();
        System.exit(returnCode);
    }

    /**
     * Handles any kind of input error, no matter where it comes from
     * @param message Message to print to console
     * @param isFileError Flag indication origin of error
     */
    public void handleInputError(String message, boolean isFileError) {
        if( isFileError && appContext.getAppOptions().isOptionFailOnFileError()             // fail on file error
             || !isFileError && appContext.getAppOptions().isOptionFailOnInputError()) {    // fail on input error

            // print msg, cleanup context and exit
            exitApp(message, -1);
        } else {

            // just print message
            System.out.println(message);
        }
    }

    public void printCurrencyToConsole(String ccy, BigDecimal amount, BigDecimal exchangeRate){
        System.out.println(formatCurrencyOutput(ccy, amount, exchangeRate));
    }

    /**
     * Formats ccy for output. Take care for conversion and rounding.
     * @param ccy Currency
     * @param amount Amount
     * @param exchangeRate Exchange rate to BASE ccy
     * @return Formatted string
     */
    public String formatCurrencyOutput(String ccy, BigDecimal amount, BigDecimal exchangeRate){
        return

            // print basic balance
            ccy + " " + amount.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() +

            // print exchange rate if present
            (exchangeRate == null ? "" : " (" + AppOptions.BASE_CURRENCY + " "
                    + amount.multiply(exchangeRate)
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
                        .stripTrailingZeros()
                        .toPlainString() + ")");
    }
}
