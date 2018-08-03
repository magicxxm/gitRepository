package com.mushiny.wms.masterdata.ibbasics.web;

import com.mushiny.wms.masterdata.ibbasics.business.dto.ItemDataAmountDTO;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ReceivingGoodsDTO;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ScanningReceivingStationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveDestinationDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceivingProcessContainerDTO;
import com.mushiny.wms.masterdata.ibbasics.service.ReceivingOperationService;
import com.mushiny.wms.masterdata.ibbasics.service.ReceivingVerificationService;
//import com.mushiny.wms.masterdata.mdbasics.crud.dto.ContainerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/masterdata/inbound/receive")
public class ReceivingController {

    private final ReceivingVerificationService receivingVerificationService;
    private final ReceivingOperationService receivingOperationService;

    @Autowired
    public ReceivingController(ReceivingVerificationService receivingVerificationService,
                               ReceivingOperationService receivingOperationService) {
        this.receivingVerificationService = receivingVerificationService;
        this.receivingOperationService = receivingOperationService;
    }

    @RequestMapping(value = "/activating/dn",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdviceRequestDTO> activateAdviceNo(@RequestParam String adviceNo) {
        return ResponseEntity.ok(receivingOperationService.activateAdviceNo(adviceNo));
    }

    @RequestMapping(value = "/receiving-goods",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> receivingGoodsToStockUnit(@RequestBody ReceivingGoodsDTO receiving) {
        receivingOperationService.receivingGoods(receiving);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/receiving-process-container",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveReceivingProcessContainer(@RequestBody ReceivingProcessContainerDTO receiving) {
        receivingOperationService.saveReceivingProcessContainer(receiving);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/receiving-process-containers",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteReceivingProcessContainers(@RequestParam String receivingStationId) {
        receivingOperationService.deleteReceivingProcessContainers(receivingStationId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/receiving-process-container",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> replaceContainer(@RequestParam String oldContainerId,
                                                 @RequestParam String newContainerId) {
        receivingOperationService.replaceContainer(oldContainerId, newContainerId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/receiving-process-containers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReceivingProcessContainerDTO>> getReceivingProcessContainers(
            @RequestParam String receivingStationId) {
        return ResponseEntity.ok(receivingVerificationService.getReceivingProcessContainers(receivingStationId));
    }

//    @RequestMapping(value = "/checking/container",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ContainerDTO> checkContainer(@RequestParam String containerId,
//                                                       @RequestParam String itemDataId,
//                                                       @RequestParam(required = false) String useNotAfter) {
//        LocalDate localDate = null;
//        if (useNotAfter != null) {
//            localDate = LocalDate.parse(useNotAfter);
//        }
//        return ResponseEntity.ok(receivingVerificationService.checkReceivingContainer(
//                containerId, itemDataId, localDate));
//    }

    @RequestMapping(value = "/screening/receiving-destination",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveDestinationDTO> screeningReceivingDestination(@RequestParam String itemDataId,
                                                                               @RequestParam String receivingType) {
        return ResponseEntity.ok(receivingVerificationService
                .screeningReceivingDestination(itemDataId, receivingType));
    }

    @RequestMapping(value = "/scanning/dn",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdviceRequestDTO> scanAdviceRequest(@RequestParam String adviceNo) {
        return ResponseEntity.ok(receivingVerificationService.scanAdviceRequest(adviceNo));
    }

//    @RequestMapping(value = "/scanning/container",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ContainerDTO> scanContainer(@RequestParam String name) {
//        return ResponseEntity.ok(receivingVerificationService.scanContainer(name));
//    }

    @RequestMapping(value = "/scanning/receiving-station",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScanningReceivingStationDTO> scanReceivingStation(@RequestParam String name) {
        return ResponseEntity.ok(receivingVerificationService.scanReceivingStation(name));
    }

    @RequestMapping(value = "/scanning/receiving-destination",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReceiveDestinationDTO> scanReceivingDestination(@RequestParam String receivingStationId,
                                                                          @RequestParam String name) {
        return ResponseEntity.ok(receivingVerificationService.scanReceivingDestination(receivingStationId, name));
    }

    @RequestMapping(value = "/scanning/sku",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDataAmountDTO> scanAdvicePositionItemData(@RequestParam String adviceId,
                                                                        @RequestParam String itemNo) {
        return ResponseEntity.ok(receivingVerificationService.scanAdvicePositionItemData(adviceId, itemNo));
    }
}
