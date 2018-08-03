package com.mushiny.controller;

import com.mushiny.service.PodService;
import com.mushiny.service.impl.PodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tank.li on 2017/7/3.
 */
@RestController
@RequestMapping(value="/heatdegree")
public class PodHeatDegreeController {

    @Autowired
    private PodService podService;

    @RequestMapping("/pod2node")
    public String computeTargetPosition(String podId, String sectionId) {
        return podService.computeTargetAddress(podId,sectionId);
    }

}
