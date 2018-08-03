/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.util.aricojflog4j;

/**
 *
 * @author AricoChen <陈庆余 AricoChen@tom.com>
 */
public class AricoJFLog2UIAppderUtil {
    private static AricoJFLog2UIInterface I;
    public static void setAricoJFTextI(AricoJFLog2UIInterface aricoJFTextI) {
        I = aricoJFTextI;
    }
    public static void doLog(String log,String logType) {
      I.setLoggerMsg(log,logType);
    }   
}
