package com.mushiny.wms.application.web;


import com.mushiny.wms.application.business.common.InvMitemLabelBusiness;
import com.mushiny.wms.application.business.common.SfcMitemBusiness;
import com.mushiny.wms.application.domain.MushinyMessage;
import com.mushiny.wms.application.rabbitMq.RabbitMqReceiver;
import com.mushiny.wms.application.service.InstructService;
import com.mushiny.wms.common.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class InstructController {
    private final InstructService  instructService;
    private final InvMitemLabelBusiness invMitemLabelBusiness;
    private final SfcMitemBusiness sfcMitemBusiness;
    private final RabbitMqReceiver rabbitMqReceiver;
    @Autowired
    public InstructController(InstructService instructService,InvMitemLabelBusiness invMitemLabelBusiness,SfcMitemBusiness sfcMitemBusiness,RabbitMqReceiver rabbitMqReceiver) {
        this.instructService = instructService;
        this.invMitemLabelBusiness=invMitemLabelBusiness;
        this.sfcMitemBusiness=sfcMitemBusiness;
        this.rabbitMqReceiver=rabbitMqReceiver;
    }

    @PostMapping(value = "/instruct/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MushinyMessage> cancel(@RequestBody String inboundInstruct) {

        boolean result=instructService.cancelInstruct(inboundInstruct);
        String message="fail";
        MushinyMessage mm=new MushinyMessage();
        int code=0;
        if(result)
        {
            message="success";
            code=1;
        }else{
            mm.setDescribe("指令已经执行,无法取消");
        }
        mm.setCode(code);
        mm.setMessage(message);
        return  ResponseEntity.ok(mm);
    }

    @PostMapping (value = "/instruct/getInstruct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getInstruct(@RequestBody String instruct) {

        String message="fail";
        MushinyMessage mm=new MushinyMessage();
        Object result=instructService.getInstruct(instruct);
        int code=0;
        if(!ObjectUtils.isEmpty(result))
        {
            message="success";
            code=1;
        }
        mm.setCode(code);
        mm.setMessage(message);
        mm.setData(result);

        return  ResponseEntity.ok(result);
    }

    @PostMapping(value = "/sfcMitemSyn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sfcMitemSyn(@RequestBody String instruct) {

       List result= sfcMitemBusiness.receive(instruct);

        return  ResponseEntity.ok(result);
    }

    @PostMapping(value = "/invMitemLabelSyn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> invMitemLabelSyn(@RequestBody String instruct) {

        List result= invMitemLabelBusiness.receive(instruct);

        return  ResponseEntity.ok(result);
    }

    @PostMapping(value = "/instruct/updateInstruct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateInstruct(@RequestBody String instruct) {
        String message="fail";
        MushinyMessage mm=new MushinyMessage();
        Integer result=instructService.updateInstruct(instruct);
        if(result==1)
        {
            message="success";
        }
        mm.setCode(result);
        mm.setMessage(message);
        return  ResponseEntity.ok(mm);
    }


    @PostMapping(value = "/iinstructiCallBack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> instructiCallBack(@RequestBody String instruct) {
        String message="success";
        MushinyMessage mm=new MushinyMessage();

        rabbitMqReceiver.test(JSONUtil.jsonToMap(instruct));
        mm.setCode(1);
        mm.setMessage(message);
        return  ResponseEntity.ok(mm);
    }

}
