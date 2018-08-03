package com.mushiny.wms.report.web;

import com.mushiny.wms.report.query.dto.*;
import com.mushiny.wms.report.query.dto.capacityTotal.CapacityDTO;
import com.mushiny.wms.report.query.dto.pp_work.PpWorkDTO;
import com.mushiny.wms.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /*FUD*/
    @RequestMapping(value = "/fud",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FudDTO>> getByFud() {
        return ResponseEntity.ok(reportService.getFud());
    }

    /*车牌遗留数据*/
    @RequestMapping(value = "/legacyData",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FudDTO>> getByContainerLegacyData() {
        return ResponseEntity.ok(reportService.getContainerLegacyData());
    }

    /*workflow 汇总数据*/
    @RequestMapping(value = "/workflow-total",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkFlowDTO>> getByWorkFlows(@RequestParam(required = false) String startTime,
                                                            @RequestParam(required = false) String endTime,
                                                            @RequestParam(required = false) String goodsType) {
        return ResponseEntity.ok(reportService.getWorkFlows(startTime, endTime, goodsType));
    }

    /*workflow 明细数据*/
    @RequestMapping(value = "/workflow-total/detail",
            method = RequestMethod.GET,
            params = {"workflowType"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkFlowDetailDTO>> getByDetails(@RequestParam(required = false) String exsdTime,
                                                                @RequestParam(required = false) String ppName,
                                                                @RequestParam String workflowType) {
        return ResponseEntity.ok(reportService.getWorkFlowDetails(ppName, exsdTime, workflowType));
    }


    //获取 WorkPool-Process Path 合计
    @RequestMapping(value = "/work-pool/process-path",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkPpDTO>> getByWorkPps(@RequestParam(required = false) String startTime,
                                                        @RequestParam(required = false) String endTime,
                                                        @RequestParam(required = false) String goodsType) {
        return ResponseEntity.ok(reportService.getByWorkPps(startTime, endTime, goodsType));
    }

    //获取Process Path-Work Pool 合计
    @RequestMapping(value = "/process-path/work-pool",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PpWorkDTO> getPpWork(@RequestParam(required = false) String startTime,
                                               @RequestParam(required = false) String endTime,
                                               @RequestParam(required = false) String goodsType) {
        return ResponseEntity.ok(reportService.getPpWork(startTime, endTime, goodsType));
    }


    //获取 PickArea 数据
    @RequestMapping(value = "/pick-area",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickDTO>> getPickArea(@RequestParam(required = false) String ppName,
                                                     @RequestParam(required = false) String zoneName,
                                                     @RequestParam(required = false) String deliveryDate) {
        return ResponseEntity.ok(reportService.getPickArea(ppName, zoneName, deliveryDate));
    }

    //获取 PickExSD 数据
    @RequestMapping(value = "/pick-exSD",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PickDTO>> getPickExSD(@RequestParam(required = false) String ppName,
                                                     @RequestParam(required = false) String zoneName,
                                                     @RequestParam(required = false) String deliveryDate) {
        return ResponseEntity.ok(reportService.getPickExSD(ppName, zoneName, deliveryDate));
    }

    //获取Capacity-total 数据
    @RequestMapping(value = "/capacity",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CapacityDTO>> getCapacity() {
        return ResponseEntity.ok(reportService.getCapacity());
    }

    //获取Capacity-Pod 数据
    @RequestMapping(value = "/capacity/pods",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CapacityPodDTO>> getPods() {
        return ResponseEntity.ok(reportService.getPods());
    }

    //获取Capacity-pod-side 所有面 数据
    @RequestMapping(value = "/capacity/pods/sides",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CapacityPodDTO>> getByPodName(@RequestParam(required = false) String podName) {
        return ResponseEntity.ok(reportService.getByPodName(podName));
    }

    //获取某个pod下 -> 某个面下 -> 所有bin(货位) 数据
    @RequestMapping(value = "/capacity/pods/sides/bins",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CapacityPodDTO>> getByPodNameAndFace(
            @RequestParam(required = false) String podName) {
        //@RequestParam(required = false) String face)
        return ResponseEntity.ok(reportService.getByPodNameAndFace(podName));
    }

}
