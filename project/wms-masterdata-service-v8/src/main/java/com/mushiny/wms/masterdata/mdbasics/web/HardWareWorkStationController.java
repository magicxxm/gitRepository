package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveThresholdDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.HardWareDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.mdbasics.service.HardWareWorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HardWareWorkStationController {

    private final HardWareWorkStationService receiveEligibilityService;

    @Autowired
    public HardWareWorkStationController(HardWareWorkStationService receiveEligibilityService) {
        this.receiveEligibilityService = receiveEligibilityService;
    }

    @RequestMapping(value = "/masterdata/workstations/{id}/hardwares",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWorkStationHardWares(@PathVariable String id,
                                                       @RequestBody List<String> hardwares) {
        receiveEligibilityService.createWorkStationHardWares(id, hardwares);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/masterdata/workstation-hardware/workstations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkStationDTO>> getWorkStationList() {
        return ResponseEntity.ok(receiveEligibilityService.getWorkStationList());
    }

    @RequestMapping(value = "/masterdata/workstations/{id}/hardwares/assigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HardWareDTO>> getAssignedByWorkStation(@PathVariable String id) {
        return ResponseEntity.ok(receiveEligibilityService.getAssignedByWorkStation(id));
    }

    @RequestMapping(value = "/masterdata/workstations/{id}/hardwares/unassigned",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HardWareDTO>> getUnassignedByWorkStation(@PathVariable String id) {
        return ResponseEntity.ok(receiveEligibilityService.getUnassignedByWorkStation(id));
    }
}
