package com.mushiny.wms.internaltool.web;

import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.domain.RcsTrip;
import com.mushiny.wms.internaltool.common.domain.Robot;
import com.mushiny.wms.internaltool.service.CommonToolService;
import com.mushiny.wms.internaltool.web.dto.RobotDTO;
import com.mushiny.wms.internaltool.web.dto.TripDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal-tool/common")
public class CommonToolController {

    private final CommonToolService commonToolService;

    @Autowired
    public CommonToolController(CommonToolService commonToolService) {
        this.commonToolService = commonToolService;
    }

    @RequestMapping(value = "/itemdata",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemData> getItemData(@RequestParam String itemNo,
                                                @RequestParam String clientId,
                                                @RequestParam String warehouseId) {
        return ResponseEntity.ok(commonToolService.getByItemNo(itemNo, clientId, warehouseId));
    }

    //调度单
    @RequestMapping(value="/findTrip",
            method=RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripDTO>> getTrip(@RequestParam String seek){
        return ResponseEntity.ok(commonToolService.getRcsTrip(seek));
    }

    //小车电量
    @RequestMapping(value="/robotLaveBattery",
            method=RequestMethod.GET,
            params = {"robotId"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RobotDTO>> getLaveBattery(@RequestParam String robotId){
        return ResponseEntity.ok(commonToolService.getLaveBattery(robotId));
    }
}
