package cz.teejays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AppOptionsTest{

    @Test
    void testDefaults() {
        AppOptions options = new AppOptions(Arrays.asList(new String[0]));

        assertFalse(options.isOptionFailOnFileError());
        assertFalse(options.isOptionFailOnInputError());
        assertFalse(options.isOptionInitializationFileUsed());
        assertFalse(options.isOptionRealNumbersAllowed());
    }

    @Test
    void testIncorrectFileInput() {

        // exception is expected
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            AppOptions options = new AppOptions(Arrays.asList(new String[]{"-f"}));
        });
        assertEquals("No input file specified", exception.getMessage());
    }

    @Test
    void testFileInput() {

        AppOptions options = new AppOptions(Arrays.asList(new String[]{"-f", "/home/root/myfile.txt"}));

        assertTrue(options.isOptionInitializationFileUsed());
        assertEquals("/home/root/myfile.txt", options.getInitializationFilePath());
    }
}