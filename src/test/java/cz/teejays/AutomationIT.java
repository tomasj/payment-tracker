package cz.teejays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Check whether app behaves correctly when used by automation tool. Close to E2E testing.
 */
class AutomationIT {

    // process (java -jar ...)
    Process process;

    // streams
    InputStream in;
    InputStream err;
    OutputStream out;

    // scanners
    Scanner inputScanner;
    Scanner errInputScanner;

    /**
     * Basic use case. Run app, enter command, read output, exit.
     */
    @Test
    void basic() {
        try {

            // start process
            initProcess("java -jar target/payment-tracker.jar");

            // app lives?
            assertTrue(process.isAlive());

            // app records ok? - write command and wait for periodic display
            write("czk 100", out);
            Thread.sleep((AppOptions.DISPLAY_INTERVAL + 1) * 1000); // wait for periodic display

            // app records ok? - read input, check timeout
            assertTimeoutPreemptively(ofSeconds(5), () -> {
                assertEquals("CZK 100", readLine(inputScanner));
            } , "Timed-out: No output observed after payment command");

            // app exits?
            write("exit", out);
            Thread.sleep(1000); // give it a sec for shutdown
            assertEquals(0, process.exitValue());

        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Basic use case + input file used for initialization.
     */
    @Test
    void withInputFile() {
        try {

            // start process
            initProcess("java -jar target/payment-tracker.jar -f input.txt");

            // app lives?
            assertTrue(process.isAlive());

            // write payment cmd, wait for periodic output
            write("gbp 100", out);
            Thread.sleep((AppOptions.DISPLAY_INTERVAL + 1) * 1000); // wait for periodic display

            // read all output lines
            final List<String> lines = new ArrayList<>();
            assertTimeoutPreemptively(ofSeconds(5), () -> {
                lines.addAll(readLines(inputScanner, 4));
            } , "Input timed-out");

            // all currencies present?
            assertEquals(4, lines.size());

            // check balances
            assertTrue(lines.contains("USD 100"));
            assertTrue(lines.contains("CZK 1000 (USD 46.95)"));
            assertTrue(lines.contains("EUR 90"));
            assertTrue(lines.contains("GBP 100"));

            // app exits?
            write("exit", out);
            Thread.sleep(1000); // give it a sec for shutdown
            assertEquals(0, process.exitValue());

        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Fail flag used.
     */
    @Test
    void basicWithErrorFlag() {
        try {

            // start process
            initProcess("java -jar target/payment-tracker.jar -failOnInputError");

            // app lives?
            assertTrue(process.isAlive());

            // app records ok? - write command and wait for periodic display
            write("USD 900", out);
            Thread.sleep((AppOptions.DISPLAY_INTERVAL + 1) * 1000); // wait for periodic display

            // app records ok? - read input, check timeout
            assertTimeoutPreemptively(ofSeconds(5), () -> {
                assertEquals("USD 900", readLine(inputScanner));
            }, "Timed-out: No output observed after payment command");

            // send erroneous input command
            write("USDs -400", out);

            // read from error output
            assertTimeoutPreemptively(ofSeconds(5), () -> {
                assertEquals("Payment processing error: " + "Currency symbol format error", readLine(errInputScanner));
            }, "Timed-out: No output observed after payment command");

            // app exits with error code?
            Thread.sleep(1000); // give it a sec for shutdown
            assertEquals(-1, process.exitValue());

        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * End process, join its thread, cleanup
     */
    @AfterEach
    void destroy() throws IOException {
        in.close();
        err.close();
        out.close();
        process.destroy();
    }

    private void initProcess(String command) throws Exception{

        // process
        process = Runtime.getRuntime().exec(command);

        // streams
        in = process.getInputStream();
        err = process.getErrorStream();
        out = process.getOutputStream();

        // scanners
        inputScanner = new Scanner(in);
        errInputScanner = new Scanner(err);
    }

    private void write(String txt, OutputStream out) throws IOException {
        out.write((txt + System.getProperty("line.separator")).getBytes());
        out.flush();
    }

    /**
     * Read one line from scanner
     * - will freeze on hasNextLine, when scanner has no more input
     * @param scanner Input scanner
     * @return Read input line
     * @throws IOException On I/O error
     */
    private String readLine(Scanner scanner) throws IOException {
        if(scanner.hasNextLine()){
            return scanner.nextLine();
        }else{
            return null;
        }
    }

    /**
     * Read multiple lines from scanner
     * - will freeze on hasNextLine, when scanner has no more input
     * - that's why numLines param must be entered
     * @param scanner Input scanner
     * @param numLines Number of lines to read
     * @return Read input line
     * @throws IOException On I/O error
     */
    private List<String> readLines(Scanner scanner, int numLines) throws IOException {
        List<String> lines = new ArrayList<>();
        for(int i = 0; i < numLines; i++){
            if(scanner.hasNextLine()){
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }
}