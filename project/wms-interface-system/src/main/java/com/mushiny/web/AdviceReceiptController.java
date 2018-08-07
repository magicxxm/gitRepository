package com.mushiny.web;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.AdviceService;
import com.mushiny.web.dto.AdviceReceiptDTO;
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
 * Created by 123 on 2018/2/1.
 */
@RestController
@RequestMapping("/wms/mushiny")
public class AdviceReceiptController {
    private final Logger log = LoggerFactory.getLogger(AdviceReceiptController.class);

    private final AdviceService adviceService;

    @Autowired
    public AdviceReceiptController(AdviceService adviceService){
        this.adviceService = adviceService;
    }

    @RequestMapping(value = "/receipt/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> create(@RequestBody AdviceReceiptDTO dto) {
        JSONObject jsonObject = JSONObject.fromObject(dto);
        log.info("收货单同步  接收到的信息是：==> " + jsonObject);
        return ResponseEntity.ok(adviceService.createAdviceRequest(dto));
    }
}
