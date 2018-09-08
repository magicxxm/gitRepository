package com.mushiny.wms.application.web;


import com.mushiny.wms.application.domain.*;
import com.mushiny.wms.application.service.InboundInstructService;
import com.mushiny.wms.application.service.OutboundInstructService;
import com.mushiny.wms.application.service.impl.InboundInstructServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class InboundInstructController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InboundInstructController.class);
    private final InboundInstructService inboundInstructService;

    @Autowired
    public InboundInstructController(InboundInstructService inboundInstructService) {
        this.inboundInstructService = inboundInstructService;
    }

    @PostMapping(value = "/inboundInstruct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MushinyMessage> save(@RequestBody String inboundInstruct) {
        Integer result=1;
        try
        {
           inboundInstructService.saveInboundInstruct(inboundInstruct);
        }catch (Exception e)
        {
            result=0;
            LOGGER.error(e.getMessage(),e);
            throw e;
        }

        String message="fail";
        MushinyMessage mm=new MushinyMessage();
        if(result==1)
        {
            message="success";
        }
        mm.setCode(result.toString());
        mm.setMessage(message);
        return  ResponseEntity.ok(mm);
    }

    //入库指令查询
    @RequestMapping(value="instructIn",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InboundInstruct>> getInboundInstruct(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String labelNo,
                                                                    Pageable pageable){
        return ResponseEntity.ok(inboundInstructService.getByInboundLabelNo(startTime,endTime,labelNo,pageable));
    }
    //根据配送卡号查明细
    @RequestMapping(value="instructIn/position",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WmsInstructInPosition>> getInboundInstructPosition(@RequestParam String labelNo){
        return ResponseEntity.ok(inboundInstructService.getInboundByLabelNo(labelNo));
    }
}
