package com.mushiny.wms.application.web;

import com.mushiny.wms.application.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2018/7/26.
 */

@RestController
public class WmsController {
    @Autowired
    private RedisUtil redisUtil;
    @GetMapping(value = "/instructInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getInstructInfo(@RequestParam(required = false) String instructId,@RequestParam String type,@RequestParam(required = false)  String info) {
        Object result;
        if("instructIn".equalsIgnoreCase(type))
        {
            if(!StringUtils.isEmpty(info))
            {
                result=redisUtil.get("InboundInstruct",info);
            }else{
                result=redisUtil.get("InboundInstruct",instructId);
            }

        }else
        {
            if(!StringUtils.isEmpty(info))
            {
                result=redisUtil.get("OutboundInstruct",info);
            }else{
                result=redisUtil.get("OutboundInstruct",instructId);
            }
        }
        return  ResponseEntity.ok(result);
    }
}
