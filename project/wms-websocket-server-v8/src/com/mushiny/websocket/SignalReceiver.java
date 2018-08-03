package com.mushiny.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignalReceiver {
    private static Logger logger = LoggerFactory.getLogger(SignalReceiver.class);
    private static int count = 0;

    public static void startListener() {
        logger.debug("启动MQ监听....");
        while (count < 10) {
            count++;//启动四个监听即可
            Thread thread = new MqReceiveThread();
            thread.setDaemon(true);//常驻后台执行
            thread.setName("MqReceiveThread" + (count + 1));
            logger.debug("新增线程:" + thread.getName());
            thread.start();//不用线程池了
        }
        logger.info("监听启动完毕!线程数" + count);
    }
}
