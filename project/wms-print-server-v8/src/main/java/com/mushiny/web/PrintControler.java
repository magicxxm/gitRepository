package com.mushiny.web;

import com.mushiny.service.PrintService;
import com.mushiny.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2018/2/10.
 */
@RestController
@RequestMapping(value = "/printService")
public class PrintControler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrintControler.class);
    @Autowired
    private PrintService printService;

    @RequestMapping(value = "/print", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> print(@RequestBody String data) {

        LOGGER.info("接受到打印请求{}", data);
        if (!StringUtils.isEmpty(data)) {
            printService.print(JSONUtil.jsonToMap(data));
        }
        return ResponseEntity.ok(1);
    }
   /* public ResponseEntity<Integer> print(@RequestParam("data")String ip,@RequestParam("type")String type,@RequestParam("goodsDescript")String goodsDescript,@RequestParam("goodsItemNo")String goodsItemNo){
        Map<String,String> param=new HashMap<>();
        param.put("ip",ip);
        param.put("type",type);
        param.put("goodsDescript",goodsDescript);
        param.put("goodsItemNo",goodsItemNo);

        if(!StringUtils.isEmpty(param))
        {
            printService.print(param);
        }
        return ResponseEntity.ok(1);
    }*/
}
