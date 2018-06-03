## Building the app

_Requirements: Java 8+, Maven 3+_ 

Use maven to build the app: `mvn clean package verify`

Build results will be generated to directory `target/`.
Javadoc located under `target/site/apidocs`


## Running the app
After successful build, app's JAR file will be located at `target/payment-tracker.jar`. Run it with `java -jar payment-tracker.jar`

Optionally a file can be used initialize app's starting state `java -jar payment-tracker.jar -f myfile.txt`

To configure app behaviour, other flags can also be used:

| Flag                  |                                                           |
| -------------         | -------------                                             |
| -f <path/filename>    | initializes app with input file                           |
| -failOnFileError      | app fails when any error found in input file              |
| -failOnInputError     | app fails when erroneous user input is observed           |

## Notes
* Balances and exchange rates are stored with [almost unlimited](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html) precision. Displayed values are rounded to max. 2 decimal places using "half up" method.
* UTF-8 character encoding is assumed for both file and console input.




