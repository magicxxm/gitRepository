package com.mushiny.wms.pathPlanning.service.impl;
import com.mushiny.wms.pathPlanning.business.PathPlanningBusiness;
import com.mushiny.wms.pathPlanning.business.TurnPanningBusiness;
import com.mushiny.wms.pathPlanning.service.PlanningService;
import com.mushiny.wms.pathPlanning.utils.MapNodeUtils;
import com.mushiny.wms.pathPlanning.utils.NodeCosteValue;
import com.mushiny.wms.pathPlanning.utils.UpdateCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PlanningServiceImpl implements PlanningService {

    private final PathPlanningBusiness pathPlanningBusiness;
    private final TurnPanningBusiness turnPanningBusiness;
    private final MapNodeUtils mapNodeUtils;

    @Autowired
    public PlanningServiceImpl(TurnPanningBusiness turnPanningBusiness,
                               PathPlanningBusiness pathPlanningBusiness, MapNodeUtils mapNodeUtils) {
        this.turnPanningBusiness = turnPanningBusiness;
        this.pathPlanningBusiness = pathPlanningBusiness;
        this.mapNodeUtils = mapNodeUtils;
    }

    @Override
    public List<Integer> getEmptyDrivePath(String warehouseId, String sectionId, int sourceVertex, int targetVertex) {
        return pathPlanningBusiness.getEmptyDrivePath(warehouseId, sectionId, sourceVertex, targetVertex);
    }

    @Override
    public List<Integer> getHeavyDrivePath(String warehouseId, String sectionId, int sourceVertex, int targetVertex) {
        return pathPlanningBusiness.getHeavyDrivePath(warehouseId, sectionId, sourceVertex, targetVertex);
    }

    @Override
    public int getPodTurning(String face, int sourceToward, int targetToward) {
        return turnPanningBusiness.getPodTurning(face, sourceToward, targetToward);
    }

    @Override
    public int updateNewCost(String warehouseId, String sectionId, String addressList, String newCost) {
            return mapNodeUtils.updateMapNeighborBySectionId(sectionId, addressList, newCost);
    }

    @Override
    public int updateHeavyDriveCost(String warehouseId, String sectionId, String addressList, String newCost) {
        return mapNodeUtils.updateHeavyDriveMapNeighborBySectionId(sectionId, addressList, newCost);
    }

    @Override
    public String getMapNode(String sectionId,Integer add) {
        return mapNodeUtils.printMapNodeBySectionId(sectionId,add);
    }

    @Override
    public List<Map<String,Object>> getMapNeighbor(String sectionId,Integer add) {

         return mapNodeUtils.getMapNeighborByMapId(sectionId,add);

    }

    @Override
    public List<UpdateCost> getUpdateNodeCost() {
        return mapNodeUtils.getUpdateNodeCost();
    }

    @Override
    public List<UpdateCost> getRecoverNodeCost() {
        return mapNodeUtils.getRecoverNodeCost();
    }

    @Override
    public List<NodeCosteValue> getMinCost(String sectionId, Integer start, Integer end) {
        return mapNodeUtils.getMinCost(sectionId,start,end);
    }

    @Override
    public int updateMap() {
        mapNodeUtils.init();
        return 1;
    }

    @Override
    public List<Integer> getChangedCostValue(String sectionId, Integer changeValue) {
        return mapNodeUtils.getChangedCostValue(sectionId,changeValue);
    }
    public Map<String,Object> getHeavyDrivePathDetail(String warehouseId, String sectionId,
                                                                int sourceVertex, int targetVertex)
    {
        return mapNodeUtils.getDrivePathDetail(sectionId,sourceVertex,targetVertex,1);
    }

    @Override
    public Map<String,Object> getEmptyDrivePathDetail(String sectionId, String paths) {
        return mapNodeUtils.getPathPairDetail(sectionId,paths,0);
    }

    @Override
    public Map<String,Object> getHeavyDrivePathDetail(String sectionId, String paths) {
        return mapNodeUtils.getPathPairDetail(sectionId,paths,1);
    }

    public Map<String,Object> getEmptyDrivePathDetail(String warehouseId, String sectionId,
                                                                int sourceVertex, int targetVertex)
    {
        return mapNodeUtils.getDrivePathDetail(sectionId,sourceVertex,targetVertex,0);
    }
}
