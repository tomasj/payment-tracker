package cz.teejays.components;

import cz.teejays.AppContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileInputProcessor {

    private AppContext appContext;

    public FileInputProcessor(AppContext appContext) {
        this.appContext = appContext;
    }

    public void processFile(){

        File file = new File(appContext.getAppOptions().getInitializationFilePath());

        // file exist?
        if(!file.exists()){
            this.appContext.getOutputHandler().handleInputError("File with initial content not found: " + appContext.getAppOptions().getInitializationFilePath(), true);
        }

        // read lines
        try {
            Files.lines(file.toPath()).forEach( line -> appContext.getInputProcessor().processInputLine(line, true) );
        } catch (IOException e) {
            this.appContext.getOutputHandler().handleInputError("Error while reading input file", true);
        }
    }

}
