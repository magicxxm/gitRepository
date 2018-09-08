package test.com.mushiny.wms.application.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.com.mushiny.wms.application.common.Constant;
import test.com.mushiny.wms.application.test.service.InBoundService;

/**
 * @program: wms-midea-server
 * @description: 入库接口测试控制器
 * @author: mingchun.mu@mushiny.com
 * @create: 2018-07-09 20:16
 **/

@RestController
public class InBoundController {

    @Autowired
    private InBoundService inBoundService;



    /***
    * 空料车到达上架工作站
    * @Author: mingchun.mu@mushiy.com
    */
    @GetMapping(value = "/emptyPod2InBoundStation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> emptyPod2InBoundStation(@RequestBody String podId){
        inBoundService.addEmptyPodOfInboundStation(podId);
        inBoundService.sendInboundCommand(podId);
        return ResponseEntity.ok(Constant.SUCCESS_FLAG + "");
    }







}
