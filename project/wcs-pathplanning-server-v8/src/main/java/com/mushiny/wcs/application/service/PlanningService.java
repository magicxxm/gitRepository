package com.mushiny.wcs.application.service;

import com.mushiny.wcs.application.domain.PathPair;
import com.mushiny.wcs.application.utils.NodeCosteValue;
import com.mushiny.wcs.application.utils.UpdateCost;

import java.util.List;
import java.util.Map;

public interface PlanningService {

    List<Integer> getEmptyDrivePath(String warehouseId, String sectionId, int sourceVertex, int targetVertex);

    List<Integer> getHeavyDrivePath(String warehouseId, String sectionId, int sourceVertex, int targetVertex);

    int getPodTurning(String face, int sourceToward, int targetToward);

    int updateNewCost(String warehouseId, String sectionId, String addressList, String newCost);
    int updateHeavyDriveCost(String warehouseId, String sectionId, String addressList, String newCost);

    String getMapNode(String sectionId,Integer add);
    List<Map<String,Object>> getMapNeighbor(String sectionId,Integer add);
    List<UpdateCost> getUpdateNodeCost();
    List<UpdateCost>  getRecoverNodeCost();
    List<NodeCosteValue>  getMinCost(String sectionId,Integer start,Integer end);
    int updateMap();
    List<Integer> getChangedCostValue(String sectionId, Integer changeValue);
    java.util.Map<String,Object> getEmptyDrivePathDetail(String warehouseId, String sectionId,
                                                                int sourceVertex, int targetVertex);
    java.util.Map<String,Object> getHeavyDrivePathDetail(String warehouseId, String sectionId,
                                                                int sourceVertex, int targetVertex);
    Map<String,Object> getEmptyDrivePathDetail(String sectionId, String paths);
    Map<String,Object> getHeavyDrivePathDetail(String sectionId,String paths);
}
