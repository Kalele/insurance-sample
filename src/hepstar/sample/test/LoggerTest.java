package hepstar.sample.test;

import hepstar.sample.application.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoggerTest {

    @Test
    public void testLogInfo() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        Logger.logInfo("This is an info message");

        String expectedOutput = "[INFO] This is an info message\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testLogError() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setErr(printStream);

        Logger.logError("This is an error message");

        String expectedOutput = "[ERROR] This is an error message\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testLogDebug() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        Logger.logDebug("This is a debug message");

        String expectedOutput = "[DEBUG] This is a debug message\n";
        assertEquals(expectedOutput, outputStream.toString());
    }
}