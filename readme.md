## Building the app

_Requirements: Java 8+, Maven 3+ available_ 

Use maven to build the app:
`mvn clean install`

Generate JavaDoc with: `mvn javadoc:javadoc`

Build results will be generated to directory `target/`


## Running the app
After successful build, app's JAR file will be located at `target/payment-tracker.jar`. Run it with `java -jar payment-tracker.jar`

Optionally a file can be used initialize app's starting state `java -jar payment-tracker.jar -f myfile.txt`

To configure app behaviour, other flags can also be used:

| Flag                  |                                                           |
| -------------         | -------------                                             |
| -f <path/filename>    | initializes app with input file                           |
| -failOnFileError      | app fails when any error found in input file              |
| -failOnInputError     | app fails when erroneous user input is observed           |
| -realNumbersAllowed   | won't treat decimal separator in user's input as an error |

## Notes / Assumptions
If `-realNumbersAllowed` flag is not used, any real number processed will be rounded to nearest whole number (half up rounding).

No assumptions were taken so far.




