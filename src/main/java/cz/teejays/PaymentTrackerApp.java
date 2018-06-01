package cz.teejays;

import cz.teejays.components.*;

import java.util.Arrays;

public class PaymentTrackerApp {

    //
    private static AppContext appContext;

    /**
     * CLI entry point. Parse arguments, initialize app context and start it.
     * @param args App's arguments
     */
    public static void main(String[] args){

        // process arguments
        AppOptions appOptions = null;
        try {
            appOptions = new AppOptions(Arrays.asList(args));
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            printUsage();
            System.exit(-1);
        }

        // context initialization phase
        initializeContext(appOptions);

        // app start
        appContext.start();
    }

    /**
     * Initialize needed app components
     * @param appOptions App options and flags
     */
    public static void initializeContext(AppOptions appOptions){
        appContext = new AppContext(appOptions);
        appContext.setOutputHandler(new OutputHandler(appContext));
        if(appOptions.isOptionInitializationFileUsed()){
            appContext.setFileInputProcessor(new FileInputProcessor(appContext));
        }
        appContext.setPaymentTracker(new PaymentTracker());
        appContext.setInputProcessor(new InputProcessor(appContext));
        appContext.setPeriodicPaymentDisplay(new PeriodicPaymentDisplay(appContext));
    }

    /**
     * Print app's CLI usage
     */
    public static void printUsage(){
        System.out.println("Usage: java -jar teejays-test.jar [-f <myfile.txt>] [-failOnFileError] [-failOnInputError] [-realNumbersAllowed]");
        System.out.println("  options: -failOnFileError     App fails when any error found in input file ");
        System.out.println("           -failOnInputError    App fails when erroneous user input is observed ");
        System.out.println("           -realNumbersAllowed  Won't treat decimal separator in user's input as an error");
    }
}
