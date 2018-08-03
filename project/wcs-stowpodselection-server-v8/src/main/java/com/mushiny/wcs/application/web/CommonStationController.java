package com.mushiny.wcs.application.web;

import com.mushiny.wcs.application.service.CommonService;
import com.mushiny.wcs.application.timer.TestTimer;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommonStationController {

    private final CommonService commonService;
    private final TestTimer testTimer;

    @Autowired
    public CommonStationController(CommonService commonService,TestTimer testTimer) {
        this.commonService = commonService;
        this.testTimer=testTimer;
    }

    @PostMapping(value = "/commonStation/calling/pods", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> callPods(@RequestBody String callData) {
        commonService.execute(JSONUtil.jsonToMap(callData));
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/callPod/calling/pods", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> start(@RequestParam("sectionId") String sectionId,@RequestParam("wareHouseId") String wareHouseId) {
        testTimer.start(sectionId,wareHouseId);
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/callPod/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> stop() {

        return ResponseEntity.ok().build();
    }
}
