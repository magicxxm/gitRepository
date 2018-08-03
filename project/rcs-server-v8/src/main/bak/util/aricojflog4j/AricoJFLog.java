/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.util.aricojflog4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 *
 * @author AricoChen <陈庆余 AricoChen@tom.com>
 */
public class AricoJFLog {
    private static AricoJFLog aricoJFLog = null;
    private static Logger logger = null;
    static public AricoJFLog getLogger(String name) {
        if (logger == null) {
            logger = Logger.getLogger(name);
        }
        if (aricoJFLog==null) {
            aricoJFLog = new AricoJFLog();
        }
        return aricoJFLog;
    }
    public AricoJFLog() {
        
    }
    private static class AricoJFLevel extends Level {

        private static final long serialVersionUID = 1076913470822079835L;

        private AricoJFLevel(int level, String name, int sysLogLevel) {
            super(level, name, sysLogLevel);
        }
    }
    /*
     log4j的级别值
     OFF = new Level(2147483647, "OFF", 0);
     FATAL = new Level(50000, "FATAL", 0);
     ERROR = new Level(40000, "ERROR", 3);
     WARN = new Level(30000, "WARN", 4);
     INFO = new Level(20000, "INFO", 6);
     DEBUG = new Level(10000, "DEBUG", 7);
     TRACE = new Level(5000, "TRACE", 7);
     ALL =  不知道是多少
     */
    // 所以我们自己的级别可以大于INFO
    private static final Level AricoJFLevel = new AricoJFLevel(21000,
            "AricoJFLevel", AricoJFLog2UIAppender.LOG_LOCAL0);
    
    
    public static void fatal(Object pm_objLogInfo) {
        logger.fatal(pm_objLogInfo);
    }
   public static void erro(Object pm_objLogInfo) {
        logger.error(pm_objLogInfo);
    }
   public static void warn(Object pm_objLogInfo) {
        logger.warn(pm_objLogInfo);
    }
    public static void aricoJFLog(Object pm_objLogInfo) {
        logger.log(AricoJFLevel, pm_objLogInfo);
    }
    public static void info(Object pm_objLogInfo) {
        logger.info(pm_objLogInfo);
    }
    public static void debug(Object pm_objLogInfo) {
        logger.debug(pm_objLogInfo);
    }
    public static void trace(Object pm_objLogInfo) {
        logger.trace(pm_objLogInfo);
    }
    

    /*public static void main(String[] args) {
        AricoJFLog log = new AricoJFLog();
        log.aricoJFLog("serious ****** ");
    }*/
}
