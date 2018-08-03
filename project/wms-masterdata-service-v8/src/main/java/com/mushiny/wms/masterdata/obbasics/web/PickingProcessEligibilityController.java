package com.mushiny.wms.masterdata.obbasics.web;

import com.mushiny.wms.masterdata.general.crud.dto.UserGroupDTO;
import com.mushiny.wms.masterdata.general.domain.UserGroup;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ProcessPathDTO;
import com.mushiny.wms.masterdata.obbasics.service.PickingProcessEligibilityService;
import com.mushiny.wms.masterdata.general.crud.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/outbound/picking-process-eligibility")
public class PickingProcessEligibilityController {

    private final PickingProcessEligibilityService pickingEligibilityService;

    @Autowired
    public PickingProcessEligibilityController(PickingProcessEligibilityService pickingEligibilityService) {
        this.pickingEligibilityService = pickingEligibilityService;
    }

    @RequestMapping(value = "/process-paths/{id}/users",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPickingAreaEligibility(@PathVariable String id,
                                                             @RequestBody List<String> users) {
        pickingEligibilityService.createPickingAreaEligibility(id, users);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/process-paths/{id}/users/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAssignedUserByProcessPathId(@PathVariable String id) {
        return ResponseEntity.ok(pickingEligibilityService.getAssignedUserByProcessPathId(id));
    }
    @RequestMapping(value = "/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserGroupDTO>> getUserGroup() {
        return ResponseEntity.ok(pickingEligibilityService.getUserGroup());
    }
    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"userGroupId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserByUserGroupId(@RequestParam String userGroupId){
        return ResponseEntity.ok(pickingEligibilityService.getUserByUserGroupId(userGroupId));
    }
    @RequestMapping(value = "/process-paths/{id}/users/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUnassignedUserByProcessPathId(@PathVariable String id) {
        return ResponseEntity.ok(pickingEligibilityService.getUnassignedUserByProcessPathId(id));
    }
    @RequestMapping(value = "/process-paths/{id}/usersID/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessPathDTO>> getAssignedPPByUserId(@PathVariable String id) {
        return ResponseEntity.ok(pickingEligibilityService.getAssignedPPByUserId(id));
    }

    @RequestMapping(value = "/process-paths/{id}/usersID/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessPathDTO>> getUnassignedPPByUserId(@PathVariable String id) {
        return ResponseEntity.ok(pickingEligibilityService.getUnassignedPPByUserId(id));
    }
    @RequestMapping(value = "/process-paths/{id}/processPath",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> savePPToUser(@PathVariable String id,
                                                             @RequestBody List<String> processPath) {
        pickingEligibilityService.savePPToUser(id, processPath);
        return ResponseEntity.ok().build();
    }
}
