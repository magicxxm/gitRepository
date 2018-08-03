package com.mushiny.wcs.application.business;

import com.mushiny.wcs.application.respository.MapNeighborRepository;
import com.mushiny.wcs.application.respository.MapNodeRepository;
import com.mushiny.wcs.application.respository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/11/13.
 */
@Component

public class CommonBusiness {
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
    public CommonBusiness(MapNeighborRepository mapNeighborRepository,
                          MapRepository mapRepository,
                          MapNodeRepository mapNodeRepository) {
        this.mapNeighborRepository = mapNeighborRepository;
        this.mapRepository = mapRepository;
        this.mapNodeRepository = mapNodeRepository;

    }
}
