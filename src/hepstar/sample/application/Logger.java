package hepstar.sample.application;

public class Logger {

    public static void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }

    public static void logDebug(String message) {
        System.out.println("[DEBUG] " + message);
    }
}