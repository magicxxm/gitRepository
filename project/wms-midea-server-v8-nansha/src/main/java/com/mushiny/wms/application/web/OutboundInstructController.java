package com.mushiny.wms.application.web;


import com.mushiny.wms.application.domain.*;
import com.mushiny.wms.application.service.InboundTripService;
import com.mushiny.wms.application.service.OutboundInstructService;
import com.mushiny.wms.application.service.OutboundTripService;
import com.mushiny.wms.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;


@RestController
public class OutboundInstructController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundInstructController.class);
    private final OutboundInstructService outboundInstructService;

    @Autowired
    public OutboundInstructController(OutboundInstructService outboundInstructService) {
        this.outboundInstructService = outboundInstructService;

    }


    @PostMapping(value = "/outboundInstruct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MushinyMessage> save(@RequestBody String inboundInstruct) {
        Integer result=1;
        try
        {
           outboundInstructService.saveOnboundInstruct(inboundInstruct);
        }catch (Exception e)
        {
            result=0;
            LOGGER.error(e.getMessage(),e);
            throw e;
        }

        String message="fail";
        MushinyMessage mm=new MushinyMessage();
        if(result==1) {
            message = "success";
        }
        mm.setCode(result.toString());
        mm.setMessage(message);
        return  ResponseEntity.ok(mm);
    }




    @PostMapping(value = "/outboundInstructJob", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MushinyMessage> saveJob(@RequestBody String inboundInstruct) {

        return save(URLDecoder.decode(inboundInstruct));
    }

    //出库指令查询
    @RequestMapping(value="instructOut",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<OutboundInstruct>> getInboundInstruct(@RequestParam String startTime,
                                                                     @RequestParam String endTime,
                                                                     @RequestParam String labelNo,
                                                                     Pageable pageable){
        return ResponseEntity.ok(outboundInstructService.getByOutboundLabelNo(startTime,endTime,labelNo,pageable));
    }
    //根据配送卡号查明细
    @RequestMapping(value="instructOut/position",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WmsInstructOutPosition>> getInboundInstructPosition(@RequestParam String labelNo){
        return ResponseEntity.ok(outboundInstructService.getOutboundByLabelNo(labelNo));
    }

}
