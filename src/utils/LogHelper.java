package utils;

import utils.Constants;

/** Class to handle logging */
public class LogHelper {
    /** Default constructor for JavaDoc */
    public LogHelper(){
    }

    /**
     * Log a message at info level
     *
     * @param message message to log
     */
    public static void log(String message){
        Constants.SERVER_LOGGER.info(message);
    }

    /**
     * Log a message at warning level
     *
     * @param message message to log
     */
    public static void warn(String message){
        Constants.SERVER_LOGGER.warning(message);
    }

    /**
     * Log a message at severe level
     *
     * @param message message to log
     */
    public static void severe(String message){
        Constants.SERVER_LOGGER.severe(message);
    }
}
