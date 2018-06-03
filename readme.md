## Building the app

_Requirements: Java 8+, Maven 3+_ 

Use maven to build the app: `mvn clean package verify`

Build results will be generated to directory `target/`.
Javadoc is located under `target/site/apidocs`.


## Running it
After successful build, app's JAR file will be located at `target/payment-tracker.jar`. Run it with `java -jar payment-tracker.jar`

Optionally a file can be used initialize app's starting state `java -jar payment-tracker.jar -f myfile.txt`

To configure app behaviour, other flags can also be used:

| Flag                  |                                                           |
| -------------         | -------------                                             |
| -f <path/filename>    | initializes app with input file                           |
| -failOnFileError      | app fails when any error found in input file              |
| -failOnInputError     | app fails when erroneous user input is observed           |

## Commands

_Note: Following input lines can be used both from command line and initialization input file._

**Tracking payments**
```
USD 100
USD -50
usd 200
USD 17,25
USD 17.25
USD 17.333333
```

**Updating exchange rate**

_Note: USD must be used on one side of conversion pair!_
```
X USD/CZK 20
X USD/CZK 20,35
X USD/CZK 20,333333
X CZK/USD 0,05
X CZK/USD 0.05
X CZK/USD 0.055535
x usd/czk 20,35000
```

**Reading app's output**

_Note: For purpose of displaying, decimal numbers are always rounded to nearest 2 decimal place "half up"._
```
CHF 100
USD 250.5
GBP 88.62
CZK 100 (USD 4.67)
```

## Notes
* Balances and exchange rates are stored with [almost unlimited](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html) precision. Displayed values are rounded to max. 2 decimal places using "half up" method.
* UTF-8 character encoding is assumed for both file and console input.




