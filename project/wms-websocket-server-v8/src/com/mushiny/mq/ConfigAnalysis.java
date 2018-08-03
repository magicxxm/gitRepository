package com.mushiny.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Tank.li on 2017/6/15.
 */
public class ConfigAnalysis {
    private static Logger logger = LoggerFactory.getLogger(ConfigAnalysis.class);
    private static Properties properties = new Properties();

    static {
        logger.debug("加载配置文件......");
        try {
            properties.load(ConfigAnalysis.class.getResourceAsStream("/websocket.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("配置文件加载失败!");
        }
        logger.debug("ConfigAnalysis:" + properties);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
