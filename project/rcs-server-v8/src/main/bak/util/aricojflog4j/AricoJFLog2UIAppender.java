/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.util.aricojflog4j;

import org.apache.log4j.Layout;
import org.apache.log4j.net.SyslogAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 */
//public class AricoJFLogUITextAppender extends AppenderSkeleton {
public class AricoJFLog2UIAppender extends SyslogAppender {

    /**
     * The appender swing UI.
     */
    public AricoJFLog2UIAppender() {
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    public void append(LoggingEvent event) {
        String logType = event.getLevel().toString();
        String logOutput = this.layout.format(event);
        AricoJFLog2UIAppderUtil.doLog(logOutput,logType);
        if (layout.ignoresThrowable()) {
            String[] lines = event.getThrowableStrRep();
            if (lines != null) {
                int len = lines.length;
                for (int i = 0; i < len; i++) {
                    AricoJFLog2UIAppderUtil.doLog(lines[i],logType);
                    AricoJFLog2UIAppderUtil.doLog(Layout.LINE_SEP,logType);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.log4j.Appender#close()
     */
    public void close() {
        //Opportunity for the appender ui to do any cleanup.
        /*appenderUI.close();
         appenderUI = null;*/
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.Appender#requiresLayout()
     */
    public boolean requiresLayout() {
        return true;
    }

    /**
     * Performs checks to make sure the appender ui is still alive.
     *
     * @return
     */
    private boolean performChecks() {
        return !closed && layout != null;
    }
}
