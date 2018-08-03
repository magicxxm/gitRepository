package com.mushiny.wcs.application.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2018/3/18.
 */
public class RetryRejectedExecutionHandler implements RejectedExecutionHandler
{
        private static final Logger LOGGER= LoggerFactory.getLogger(RetryRejectedExecutionHandler.class);

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                LOGGER.error("已经达到最大线程限制");

        }
}
