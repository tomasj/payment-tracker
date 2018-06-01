package cz.teejays;

import cz.teejays.components.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppContext {

    // options
    private AppOptions appOptions;

    // components
    private PaymentTracker paymentTracker;
    private FileInputProcessor fileInputProcessor;
    private InputProcessor inputProcessor;
    private OutputHandler outputHandler;
    private PeriodicPaymentDisplay periodicPaymentDisplay;

    public AppContext(AppOptions appOptions) {
        this.appOptions = appOptions;
    }

    //
    public void start(){

        // load file
        if(appOptions.isOptionInitializationFileUsed()){
            fileInputProcessor.processFile();
        }

        // start 1min displaying
        periodicPaymentDisplay.startPeriodicDisplaying();

        // start reading user's input
        inputProcessor.startReading();
    }

    //
    public void exit(){
        inputProcessor.stopReading();
        periodicPaymentDisplay.stopPeriodicDisplaying();
    }
}
