package com.mushiny.wcs.application.listener;

import com.mushiny.wcs.application.domain.MapNeighbor;
import com.mushiny.wcs.application.event.MapNeighborFlushEvent;
import com.mushiny.wcs.application.respository.MapNeighborRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/12.
 */
@Component
@Transactional
public class MapNeighborListener implements ApplicationListener<MapNeighborFlushEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapNeighborListener.class);
    @Autowired
    private MapNeighborRepository mapNeighborRepository;
    @Autowired
    private ThreadPoolTaskExecutor commonExecutor;
    private LinkedBlockingQueue<MapNeighborFlushEvent> events = new LinkedBlockingQueue(100);

    private MapNeighborFlushEvent getMapNeighborFlushEvent() {
        synchronized (events) {
            MapNeighborFlushEvent event = null;
            try {
                event = events.take();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
            return event;
        }
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {

            try {
                MapNeighborFlushEvent event = getMapNeighborFlushEvent();
                for (MapNeighbor temp : event.getMapNeighbor()) {
                    //  mapNeighborRepository.saveAndFlush(temp);
                    TimeUnit.SECONDS.sleep(1);
                    LOGGER.info("更新数据库记录成功--{} ", temp.getId());
                }

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    };

    @Override
    public void onApplicationEvent(MapNeighborFlushEvent event) {
        try {
            events.put(event);
            commonExecutor.submit(task);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }


    }
}
