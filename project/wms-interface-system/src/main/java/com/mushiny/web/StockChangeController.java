package com.mushiny.web;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.ChangeStockService;
import com.mushiny.web.dto.StockChangeInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 123 on 2018/2/7.
 */
@RestController
@RequestMapping("/wms/mushiny")
public class StockChangeController {
    private final Logger log = LoggerFactory.getLogger(AdviceReceiptController.class);
    private final ChangeStockService changeStockService;

    @Autowired
    public StockChangeController(ChangeStockService changeStockService){
        this.changeStockService = changeStockService;
    }


    @RequestMapping(value = "/stockChange/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> create(@RequestBody StockChangeInfo dto) {
        JSONObject jsonObject = JSONObject.fromObject(dto);
        log.info("库存调整  接收到的信息是：==> " + jsonObject);
        AccessDTO accessDTO = changeStockService.changeStockUnit(dto);
        return ResponseEntity.ok(accessDTO);
    }
}
