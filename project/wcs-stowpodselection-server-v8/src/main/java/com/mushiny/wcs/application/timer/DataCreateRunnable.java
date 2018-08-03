package com.mushiny.wcs.application.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/18.
 */
public class DataCreateRunnable implements Runnable{
    private static final Logger LOGGER= LoggerFactory.getLogger(DataCreateRunnable.class);


    private DriveAllocationTimer.Task task;
    @Override
    public void run() {

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("线程{}自动生成stow", Thread.currentThread().getName());
            }
            while(true)
            {
                task.run();
                TimeUnit.SECONDS.sleep(10);
            }



        }catch (Exception e) {
        LOGGER.error(e.getMessage(),e);
        }


    }

    public DriveAllocationTimer.Task getTask() {
        return task;
    }

    public void setTask(DriveAllocationTimer.Task task) {
        this.task = task;
    }


}
