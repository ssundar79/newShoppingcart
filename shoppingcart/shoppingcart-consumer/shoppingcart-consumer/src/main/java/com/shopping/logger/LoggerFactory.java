package com.shopping.logger;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.LogFactory;


@SuppressWarnings({
    "unchecked"
})
public class LoggerFactory {

    /**
     * Gets the <code>Logger</code> for the Business Tier
     * 
     * @param clazz the class requesting for the Logger
     * @return ILogger
     */
    public static ILogger getBusinessLogger(Class<?> clazz) {

        Map<String, Object> instances = (Map<String, Object>) logTypes
            .get(BUSINESS_LOG);

        return getLoggerInstance(getShortName(clazz), BUSINESS_LOG, instances);
    }

    /**
     * Gets the <code>Logger</code> for the Business Tier
     * 
     * @param clazz the class requesting for the Logger
     * @return ILogger
     */
    public static ILogger getErrorLogger(Class<?> clazz) {

        Map<String, Object> instances = (Map<String, Object>) logTypes
            .get(ERROR_LOG);

        return getLoggerInstance(getShortName(clazz), ERROR_LOG, instances);
    }

    /**
     * Release resources used
     */
    public void release() {
        log("Releasing resources ...");
        Map<String, Object> instances = null;
        for (Iterator cacheIte = logTypes.values().iterator(); cacheIte
            .hasNext(); instances.clear()) {
            instances = (Map<String, Object>) cacheIte.next();
        }

        logTypes.clear();
        log("Resources released");
    }

    protected static String getKey(Class<?> clazz) {
        return clazz.getName();
    }

    protected static String getShortName(Class<?> clazz) {
        if (clazz == null) {
            return null;
        } else {
            String fullName = clazz.getName();
            int index = fullName.lastIndexOf('.');
            return fullName.substring(++index);
        }
    }

    @SuppressWarnings("all")
    protected static void log(String s) {
    }

    /**
     * Creates a new instance of <code>Logger</code> using the
     * <code>Class</code> as the identifier
     * 
     * @param logType the log to send statements to
     * @param key identity key for the class requesting for log
     * @return the Logger instance
     */

    protected static ILogger newLoggerInstance(String logType, String key) {
        log((new StringBuilder()).append(
            "Creating new Logger instances of type, ").append(logType)
            .append(", ").append(key).toString());
        Map<String, Object> instances = (Map<String, Object>) logTypes
            .get(logType);
        org.apache.commons.logging.Log log = LogFactory.getLog(logType);
        log("Obtained Log Implementation object");
        Logger logger = new Logger(log, key);
        instances.put(key, logger);
        log((new StringBuilder()).append(
            "Logger placed in cache with key, ").append(key).toString());
        return logger;
    }

    private static ILogger getLoggerInstance(String key, String logType,
        Map instances) {
        if (instances.containsKey(key)) {
            log((new StringBuilder()).append("Instance found in ").append(
                logType).append(" with key, ").append(key).toString());
            return (Logger) instances.get(key);
        } else {
            newLoggerInstance(logType, key);
            log((new StringBuilder()).append("Instance not found in ")
                .append(logType).append(" with key, ").append(key)
                .toString());
            return (Logger) instances.get(key);
        }

    }

    public static final String BUSINESS_LOG = "POC_BIZ";
    public static final String CAMEL_LOG = "POC_CAMEL";
    public static final String ERROR_LOG = "POC_ERROR";

    private static Map<String, Object> logTypes;

    static {
        logTypes = new Hashtable<>();
        logTypes.put(BUSINESS_LOG, new Hashtable<String, Object>());
        logTypes.put(CAMEL_LOG, new Hashtable<String, Object>());
        logTypes.put(ERROR_LOG, new Hashtable<String, Object>());

    }
}
