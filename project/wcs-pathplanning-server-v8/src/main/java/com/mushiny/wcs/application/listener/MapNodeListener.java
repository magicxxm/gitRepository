package com.mushiny.wcs.application.listener;

import com.mushiny.wcs.application.event.MapNodeReflushEvent;
import com.mushiny.wcs.application.utils.MapNodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/12.
 */
@Component
public class MapNodeListener implements ApplicationListener<MapNodeReflushEvent> {
    @Autowired
    private MapNodeUtils mapNodeUtils;
    private static final Logger LOGGER = LoggerFactory.getLogger(MapNodeListener.class);

    @Override
    public void onApplicationEvent(MapNodeReflushEvent event) {

        try {
            LOGGER.info("发生地图刷新事件");
            mapNodeUtils.run(null);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
