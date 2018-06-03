package cz.teejays;

import cz.teejays.components.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppContext {

    // singleton instance
    private static AppContext instance = null;

    // options
    private AppOptions appOptions;

    // components
    private PaymentTracker paymentTracker;
    private FileInputProcessor fileInputProcessor;
    private InputProcessor inputProcessor;
    private OutputHandler outputHandler;
    private PeriodicPaymentDisplay periodicPaymentDisplay;

    protected AppContext(){}

    public static AppContext getInstance() {
        if(instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    //
    public AppContext initialize(AppOptions appOptions){
        instance.setAppOptions(appOptions);
        instance.setOutputHandler(new OutputHandler(instance));
        if(appOptions.isOptionInitializationFileUsed()){
            instance.setFileInputProcessor(new FileInputProcessor(instance));
        }
        instance.setPaymentTracker(new PaymentTracker());
        instance.setInputProcessor(new InputProcessor(instance));
        instance.setPeriodicPaymentDisplay(new PeriodicPaymentDisplay(instance));
        return instance;
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
