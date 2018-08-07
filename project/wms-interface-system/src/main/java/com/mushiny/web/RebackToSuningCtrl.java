package com.mushiny.web;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.RebackService;
import com.mushiny.web.dto.AdviceReceiptDTO;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 123 on 2018/3/15.
 */
@RestController
@RequestMapping("/wms/mushiny")
public class RebackToSuningCtrl {

    private final RebackService rebackService;

    @Autowired
    public RebackToSuningCtrl(RebackService rebackService){
        this.rebackService = rebackService;
    }


    /**
     * 重新向苏宁反馈出库单
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/shipment/reback",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> rebackShipment(@RequestParam String  orderNo) {
        return ResponseEntity.ok(rebackService.updateCustomer(orderNo));
    }

    /**
     * 重新向苏宁反馈上架单
     * @param orderNo
     * @return
     */

    @RequestMapping(value = "/receipt/reback",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> rebackReceipt(@RequestParam String  orderNo) {
        return ResponseEntity.ok(rebackService.updateInbound(orderNo));
    }
}
