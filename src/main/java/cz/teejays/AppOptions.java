package cz.teejays;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class represent app's command line options / flags
 */
@Getter
@Setter
public class AppOptions {

    // static
    public static final String BASE_CURRENCY = "USD"; // use 3 char uppercase notation
    public static final int DISPLAY_INTERVAL = 60; // in seconds
    public static final String EXIT_COMMAND = "EXIT";

    // flags
    private final String FLAG_INPUT_FILE = "-f";
    private final String FLAG_FAIL_ON_FILE_ERROR = "-failOnFileError";
    private final String FLAG_FAIL_ON_INPUT_ERROR = "-failOnInputError";

    // app options
    private boolean optionFailOnFileError = false;
    private boolean optionFailOnInputError = false;
    private boolean optionRealNumbersAllowed = false;
    private boolean optionInitializationFileUsed = false;
    private String initializationFilePath;

    public AppOptions(List<String> arguments) throws IllegalArgumentException {

        // input file present?
        if(arguments.contains(FLAG_INPUT_FILE)){

            // file specified?
            int filePathIndex = arguments.indexOf(FLAG_INPUT_FILE) + 1;
            if(filePathIndex > arguments.size() -1 ) {

                // file path index out of bounds -> we got no file name
                throw new IllegalArgumentException("No input file specified");
            }

            // set flag + file path
            setOptionInitializationFileUsed(true);
            setInitializationFilePath(arguments.get(filePathIndex));
        }

        // other flags
        if(arguments.contains(FLAG_FAIL_ON_FILE_ERROR)){ setOptionFailOnFileError(true); }
        if(arguments.contains(FLAG_FAIL_ON_INPUT_ERROR)){ setOptionFailOnInputError(true); }
    }
}
