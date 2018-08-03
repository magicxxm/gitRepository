package com.mushiny.wcs.application.event;

import com.mushiny.wcs.application.domain.MapNeighbor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/12.
 */

public class MapNeighborFlushEvent extends ApplicationContextEvent {
    private List<MapNeighbor> mapNeighbor;


    public MapNeighborFlushEvent(ApplicationContext source, List<MapNeighbor> mapNeighbor) {
        super(source);
        this.mapNeighbor = mapNeighbor;
    }

    public List<MapNeighbor> getMapNeighbor() {
        return mapNeighbor;
    }

    public void setMapNeighbor(List<MapNeighbor> mapNeighbor) {
        this.mapNeighbor = mapNeighbor;
    }
}
