package com.mushiny.web;

import com.mushiny.service.TripCreateService;
import com.mushiny.web.dto.CallPodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 123 on 2018/3/27.
 */
@RestController
@RequestMapping("/icqa/wms/mushiny")
public class TripCreateController {

    @Autowired
    private TripCreateService tripCreateService;


    @RequestMapping(value = "/callPod",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CallPodDTO> rebackShipment(@RequestParam String  podName,@RequestParam String face,
                                                    @RequestParam String stationName,@RequestParam String type) {

        CallPodDTO dto = tripCreateService.createTrip(podName,face,stationName,type);

        return ResponseEntity.ok(dto);
    }
}
