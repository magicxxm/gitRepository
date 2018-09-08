package com.mushiny.wms.pathPlanning.business;


import com.mushiny.wms.application.repository.MapNeighborRepository;
import com.mushiny.wms.application.repository.MapNodeRepository;
import com.mushiny.wms.application.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/13.
 */
@Component

public class PathCommonBusiness {
    private final MapRepository mapRepository;
    private final MapNodeRepository mapNodeRepository;
    private final MapNeighborRepository mapNeighborRepository;

    public MapRepository getMapRepository() {
        return mapRepository;
    }

    public MapNodeRepository getMapNodeRepository() {
        return mapNodeRepository;
    }

    public MapNeighborRepository getMapNeighborRepository() {
        return mapNeighborRepository;
    }

    @Autowired
    public PathCommonBusiness(MapNeighborRepository mapNeighborRepository,
                              MapRepository mapRepository,
                              MapNodeRepository mapNodeRepository) {
        this.mapNeighborRepository = mapNeighborRepository;
        this.mapRepository = mapRepository;
        this.mapNodeRepository = mapNodeRepository;

    }
}
