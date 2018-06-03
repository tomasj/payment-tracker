package cz.teejays.components;

import cz.teejays.AppContext;
import cz.teejays.AppOptions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test ability of FileInputProcessor to read lines from file and pass them to InputProcessor component for processing.
 */
class FileInputProcessorTest {

    @Test
    void processFile() {

        // app options
        AppOptions appOptions = new AppOptions(Arrays.asList("-f", "input.txt"));

        // mock app context
        AppContext appContext = mock(AppContext.class);
        when(appContext.getAppOptions()).thenReturn(appOptions);
        when(appContext.getInputProcessor()).thenReturn(mock(InputProcessor.class));

        // tested class instance
        FileInputProcessor fileInputProcessor = new FileInputProcessor(appContext);

        // trigger input file processing
        fileInputProcessor.processFile();

        // now check that every line was processed
        verify(appContext.getInputProcessor()).processInputLine(eq("USD 100"), eq(true));
        verify(appContext.getInputProcessor()).processInputLine(eq("CZK 1000"), eq(true));
        verify(appContext.getInputProcessor()).processInputLine(eq("EUR 90"), eq(true));
        verify(appContext.getInputProcessor()).processInputLine(eq("X USD/CZK 21.30"), eq(true));

    }
}