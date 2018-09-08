package com.mushiny.wms.application.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/18.
 */
public class AgvRunnable implements Runnable{
    private static final Logger LOGGER= LoggerFactory.getLogger(AgvRunnable.class);

    private String template;

    private volatile boolean canStart=true;
    @Override
    public void run() {

        try {

            while(Thread.currentThread().isInterrupted()==false&&canStart) {


                TimeUnit.SECONDS.sleep(3);

            }
        }catch (Exception e) {
        LOGGER.error(e.getMessage(),e);
        }


    }


    public boolean isCanStart() {
        return canStart;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }
}
