package com.mushiny.wcs.application.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wcs.application.domain.Map;
import com.mushiny.wcs.application.domain.MapNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionMapNode {
    private com.mushiny.wcs.application.domain.Map map;
    private MapNode currentMapNode;
    private boolean isInAddress = false;
    private List<MapNode> outSizeMapNodes = new ArrayList<>();
    private List<MapNode> inSizeMapNodes = new ArrayList<>();

    public Map getMap() {
        return map;
    }


    public void setMap(Map map) {
        this.map = map;
    }

    public MapNode getCurrentMapNode() {
        return currentMapNode;
    }

    public void setCurrentMapNode(MapNode currentMapNode) {
        this.currentMapNode = currentMapNode;
    }

    public boolean isInAddress() {
        return isInAddress;
    }

    public void setInAddress(boolean inAddress) {
        isInAddress = inAddress;
    }

    public List<MapNode> getOutSizeMapNodes() {
        return Collections.unmodifiableList(outSizeMapNodes);
    }

    public void setOutSizeMapNodes(List<MapNode> outSizeMapNodes) {
        this.outSizeMapNodes.addAll(outSizeMapNodes);
    }

    public List<MapNode> getInSizeMapNodes() {
        return Collections.unmodifiableList(inSizeMapNodes);
    }

    public void setInSizeMapNodes(List<MapNode> inSizeMapNodes) {
        this.inSizeMapNodes.addAll(inSizeMapNodes);
    }
}
