package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.StowThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.service.StowEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StowEligibilityController {

    private final StowEligibilityService stowEligibilityService;

    @Autowired
    public StowEligibilityController(StowEligibilityService stowEligibilityService) {
        this.stowEligibilityService = stowEligibilityService;
    }

    @RequestMapping(value = "/masterdata/inbound/users/{id}/stowthresholds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createClientWarehouses(@PathVariable String id,
                                                       @RequestBody List<String> thresholds) {
        stowEligibilityService.createClientWarehouses(id, thresholds);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/masterdata/inbound/user-stowthreshold/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserList() {
        return ResponseEntity.ok(stowEligibilityService.getUserList());
    }

    @RequestMapping(value = "/masterdata/inbound/users/{id}/stowthresholds/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowThresholdDTO>> getAssignedWarehouseByUserId(@PathVariable String id) {
        return ResponseEntity.ok(stowEligibilityService.getAssignedWarehouseByUserId(id));
    }

    @RequestMapping(value = "/masterdata/inbound/users/{id}/stowthresholds/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StowThresholdDTO>> getUnassignedWarehouseByUserId(@PathVariable String id) {
        return ResponseEntity.ok(stowEligibilityService.getUnassignedWarehouseByUserId(id));
    }
}
