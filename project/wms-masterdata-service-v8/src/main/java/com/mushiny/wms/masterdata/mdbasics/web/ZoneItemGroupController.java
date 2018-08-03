package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemGroupDTO;
import com.mushiny.wms.masterdata.mdbasics.service.ZoneItemGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZoneItemGroupController {

    private final ZoneItemGroupService zoneItemGroupService;

    @Autowired
    public ZoneItemGroupController(ZoneItemGroupService zoneItemGroupService) {
        this.zoneItemGroupService = zoneItemGroupService;
    }

    @RequestMapping(value = "/masterdata/zones/{id}/item-groups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createZoneItemGroup(@PathVariable String id,
                                                    @RequestBody List<String> itemGroups) {
        zoneItemGroupService.createZoneItemGroups(id, itemGroups);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/masterdata/zones/{id}/item-groups/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemGroupDTO>> getAssignedItemGroupByZoneId(@PathVariable String id) {
        return ResponseEntity.ok(zoneItemGroupService.getAssignedItemGroupByZoneId(id));
    }

    @RequestMapping(value = "/masterdata/zones/{id}/item-groups/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemGroupDTO>> getUnassignedItemGroupByZoneId(@PathVariable String id) {
        return ResponseEntity.ok(zoneItemGroupService.getUnassignedItemGroupByZoneId(id));
    }
}
