//package com.mushiny.wcs.application.web;
//
//import com.mushiny.wcs.application.service.PodSelectionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PodSelectionController {
//
//    private final PodSelectionService podSelectionService;
//
//    @Autowired
//    public PodSelectionController(PodSelectionService podSelectionService) {
//        this.podSelectionService = podSelectionService;
//    }
//
//    @GetMapping(value = "/station/calling/pods", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> callPods(@RequestParam String stationId) {
//        podSelectionService.callPods(stationId);
//        return ResponseEntity.ok().build();
//    }
//}
