package com.mushiny.wms.pathPlanning.web;


import com.mushiny.wms.pathPlanning.service.PlanningService;
import com.mushiny.wms.pathPlanning.utils.NodeCosteValue;
import com.mushiny.wms.pathPlanning.utils.UpdateCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
public class PlanningController {



    private final PlanningService planningService;

    @Autowired
    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }

    @GetMapping(value = "/path-planning/empty-drive/path", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getEmptyDrivePath(@RequestParam String warehouseId,
                                                           @RequestParam String sectionId,
                                                           @RequestParam int sourceVertex,
                                                           @RequestParam int targetVertex) {

        return ResponseEntity.ok(planningService.getEmptyDrivePath(warehouseId, sectionId, sourceVertex, targetVertex));
    }

    @GetMapping(value = "/path-planning/heavy-drive/path", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getHeavyDrivePath(@RequestParam String warehouseId,
                                                           @RequestParam String sectionId,
                                                           @RequestParam int sourceVertex,
                                                           @RequestParam int targetVertex) {
        return ResponseEntity.ok(planningService.getHeavyDrivePath(warehouseId, sectionId, sourceVertex, targetVertex));
    }
    @GetMapping(value = "/path-planning/mapNode", produces = MediaType.ALL_VALUE)
    public ResponseEntity<String> getMapNode(@RequestParam String sectionId, @RequestParam(required = false) Integer addr) {
        return ResponseEntity.ok(planningService.getMapNode( sectionId,addr));
    }
    @GetMapping(value = "/path-planning/mapNeighbor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String,Object>>> getMapNeighbor(@RequestParam String sectionId, @RequestParam(required = false) Integer addr) {
        return ResponseEntity.ok(planningService.getMapNeighbor(sectionId,addr));
    }

    @GetMapping(value = "/path-planning/update-newCost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateNewCost(@RequestParam String warehouseId,
                                                 @RequestParam String sectionId,
                                                 @RequestParam String addressList,
                                                 @RequestParam(required = false) String newCost) {
        return ResponseEntity.ok(planningService.updateNewCost(warehouseId, sectionId, addressList, newCost));
    }

    @GetMapping(value = "/path-planning/update-heavyDriveCost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateHeavyDriveCost(@RequestParam String warehouseId,
                                                        @RequestParam String sectionId,
                                                        @RequestParam String addressList,
                                                        @RequestParam(required = false) String heavyCost) {
        return ResponseEntity.ok(planningService.updateHeavyDriveCost(warehouseId, sectionId, addressList, heavyCost));
    }

    @GetMapping(value = "/path-planning/update-map", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateMap() {
        return ResponseEntity.ok(planningService.updateMap());
    }

    @GetMapping(value = "/path-planning/pod/turning", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getPodTurning(@RequestParam String face,
                                                 @RequestParam int sourceToward,
                                                 @RequestParam int targetToward) {
        return ResponseEntity.ok(planningService.getPodTurning(face, sourceToward, targetToward));
    }
    @GetMapping(value = "/path-planning/updateNodeCost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UpdateCost>> getUpdateNodeCost() {
        return ResponseEntity.ok(planningService.getUpdateNodeCost());
    }
    @GetMapping(value = "/path-planning/recoverNodeCost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UpdateCost>> getRecoverNodeCost() {
        return ResponseEntity.ok(planningService.getRecoverNodeCost());
    }
    @GetMapping(value = "/path-planning/minCost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NodeCosteValue>> getMinCost(@RequestParam String sectionId, @RequestParam Integer start, @RequestParam(required = false) Integer end)
    {
        return ResponseEntity.ok(planningService.getMinCost(sectionId,start,end));
    }
    @GetMapping(value = "/path-planning/changedCostValue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer> > getChangedCostValue(@RequestParam String sectionId, @RequestParam(required = false) Integer changeValue)
    {
        return ResponseEntity.ok(planningService.getChangedCostValue(sectionId,changeValue));
    }
    @GetMapping(value = "/path-planning/emptyDrivePathDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object> > getEmptyDrivePathDetail(@RequestParam String warehouseId,
                                                                       @RequestParam String sectionId,
                                                                       @RequestParam int sourceVertex,
                                                                       @RequestParam int targetVertex)
    {
        return ResponseEntity.ok(planningService.getEmptyDrivePathDetail(warehouseId,sectionId,sourceVertex,targetVertex));
    }
    @GetMapping(value = "/path-planning/heavyDrivePathDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object> > getHeavyDrivePathDetail(@RequestParam String warehouseId,
                                                                       @RequestParam String sectionId,
                                                                       @RequestParam int sourceVertex,
                                                                       @RequestParam int targetVertex)
    {
        return ResponseEntity.ok(planningService.getHeavyDrivePathDetail(warehouseId,sectionId,sourceVertex,targetVertex));
    }

    @GetMapping(value = "/path-planning/emptyDrivePathPairDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object> > getEmptyDrivePathDetail(@RequestParam String sectionId,
                                                                       @RequestParam String paths)
    {
        return ResponseEntity.ok(planningService.getEmptyDrivePathDetail(sectionId,paths));
    }
    @GetMapping(value = "/path-planning/heavyDrivePathPairDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object> > getHeavyDrivePathDetail(@RequestParam String sectionId,
                                                                       @RequestParam String paths)
    {
        return ResponseEntity.ok(planningService.getHeavyDrivePathDetail(sectionId,paths));
    }


}
