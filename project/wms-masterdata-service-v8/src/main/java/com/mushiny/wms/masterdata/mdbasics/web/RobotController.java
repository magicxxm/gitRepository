package com.mushiny.wms.masterdata.mdbasics.web;

import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotLaveBatteryDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TripDTO;
import com.mushiny.wms.masterdata.mdbasics.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/robot/robots")
public class RobotController {

    private final RobotService robotService;

    @Autowired
    public RobotController(RobotService robotService) {
        this.robotService = robotService;
    }

    @RequestMapping(value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RobotDTO> create(@RequestBody RobotDTO dto) {
        return ResponseEntity.ok(robotService.create(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        robotService.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RobotDTO> update(@RequestBody RobotDTO dto) {
        return ResponseEntity.ok(robotService.update(dto));
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RobotDTO> get(@PathVariable String id) {
        return ResponseEntity.ok(robotService.retrieve(id));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"search"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RobotDTO>> getByCriteria(@RequestParam String search, Sort sort) {
        return ResponseEntity.ok(robotService.getBySearchTerm(search, sort));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"page", "size"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RobotDTO>> getBySearchTerm(
            @RequestParam(required = false) String search, Pageable pageable) {
        return ResponseEntity.ok(robotService.getBySearchTerm(search, pageable));
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            params = {"robotId", "password"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> enter(@RequestParam String robotId,
                                      @RequestParam String password) {
        robotService.enter(robotId, password);
        return ResponseEntity.ok().build();
    }

    //小车电量
    @RequestMapping(value="/robotLaveBattery",
            method=RequestMethod.GET,
            params = {"robotId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RobotLaveBatteryDTO>> getLaveBattery(@RequestParam String robotId){
        return ResponseEntity.ok(robotService.getLaveBattery(robotId));
    }

    //调度单
    @RequestMapping(value="/page-trip-manager",
            method=RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TripDTO>> getPageTrip(@RequestParam String startTime,
                                                     @RequestParam String endTime,
                                                     @RequestParam List<String> type,
                                                     @RequestParam boolean isFinish,
                                                     @RequestParam String seek,
                                                     @RequestParam boolean isExport,
                                                     Pageable  pageable){
        return ResponseEntity.ok(robotService.getPageRcsTrip(startTime,endTime,type,isFinish,seek,isExport,pageable));
    }
}
