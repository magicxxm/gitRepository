package com.mushiny.wcs.application.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/12.
 */

public class MapNodeReflushEvent extends ApplicationContextEvent {
    private String eventName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public MapNodeReflushEvent(ApplicationContext source, String eventName) {
        super(source);
        this.eventName = eventName;
    }
}
