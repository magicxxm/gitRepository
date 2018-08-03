package com.mingchun.mu.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Laptop-6 on 2017/12/18.
 */
public class AGVMessageSendLogger {
    private static final Logger LOG = LoggerFactory.getLogger(AGVMessageSendLogger.class);
    public static void logInfo(String msg) {
        LOG.info(msg);
    }
    public static void logDebug(String msg) {
        LOG.debug(msg);
    }
    public static void logWarn(String msg) {
        LOG.warn(msg);
    }
    public static void logTrace(String msg) {
        LOG.trace(msg);
    }
    public static void logError(String msg) {
        LOG.error(msg);
    }
}
