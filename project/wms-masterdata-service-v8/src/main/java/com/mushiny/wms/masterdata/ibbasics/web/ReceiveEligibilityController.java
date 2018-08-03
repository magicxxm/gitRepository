package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceiveEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReceiveEligibilityController {

    private final ReceiveEligibilityService receiveEligibilityService;

    @Autowired
    public ReceiveEligibilityController(ReceiveEligibilityService receiveEligibilityService) {
        this.receiveEligibilityService = receiveEligibilityService;
    }

    @RequestMapping(value = "/masterdata/inbound/users/{id}/thresholds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createClientWarehouses(@PathVariable String id,
                                                       @RequestBody List<String> thresholds) {
        receiveEligibilityService.createClientWarehouses(id, thresholds);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/masterdata/inbound/user-threshold/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserList() {
        return ResponseEntity.ok(receiveEligibilityService.getUserList());
    }

    @RequestMapping(value = "/masterdata/inbound/users/{id}/thresholds/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveThresholdDTO>> getAssignedWarehouseByUserId(@PathVariable String id) {
        return ResponseEntity.ok(receiveEligibilityService.getAssignedWarehouseByUserId(id));
    }

    @RequestMapping(value = "/masterdata/inbound/users/{id}/thresholds/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceiveThresholdDTO>> getUnassignedWarehouseByUserId(@PathVariable String id) {
        return ResponseEntity.ok(receiveEligibilityService.getUnassignedWarehouseByUserId(id));
    }
}
