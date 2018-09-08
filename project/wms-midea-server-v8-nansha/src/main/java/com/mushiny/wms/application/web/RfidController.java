package com.mushiny.wms.application.web;


import com.mushiny.wms.application.service.RfidService;
import com.mushiny.wms.common.utils.JSONUtil;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class RfidController {

    private final RfidService rfidService;

    @Autowired
    public RfidController(RfidService rfidService) {
        this.rfidService = rfidService;
    }

    /**
     * 接收入库指令
     * @param pods
     * @return
     */
    @PostMapping(value = "/carryPod", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> carryPod(@RequestBody String pods) {

        return  ResponseEntity.ok(rfidService.saveRfidInfo(pods));
    }

    /**
     * 入库呼叫空pod
     * @Author: mingchun.mu@mushiy.com
     * @param stationNameJson
     * @return
     */
    @PostMapping (value = "/callEmptyPod", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> callEmptyPod(@RequestBody String stationNameJson) {
        return  ResponseEntity.ok(rfidService.saveRfidInfoByStationName(stationNameJson));
    }


    /**
     * 物料同步接口
     * @Author: mingchun.mu@mushiy.com
     * @param request
     * @param LAST_UPDATE_DATE
     * @param INV_ORG_ID
     * @return
     */
    @GetMapping(value = "/mItems", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> mItems(HttpServletRequest request, String LAST_UPDATE_DATE, String INV_ORG_ID) {

        System.out.println(" 请求参数LAST_UPDATE_DATE： " + LAST_UPDATE_DATE);
        System.out.println(" 请求参数INV_ORG_ID： " + INV_ORG_ID);

        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        map.put("INV_ORG_ID", "1");
        map.put("MITEM_CODE", "1");
        map.put("MITEM_DESC", "物料名称1");
        map.put("UOM", "UOM1");
        map.put("MITEM_STATUS", "1");
        list.add(map);
        map = new HashMap<>();
        map.put("INV_ORG_ID", "2");
        map.put("MITEM_CODE", "2");
        map.put("MITEM_DESC", "物料名称2");
        map.put("UOM", "UOM2");
        map.put("MITEM_STATUS", "2");
        list.add(map);
        map = new HashMap<>();
        map.put("INV_ORG_ID", "3");
        map.put("MITEM_CODE", "3");
        map.put("MITEM_DESC", "物料名称3");
        map.put("UOM", "UOM3");
        map.put("MITEM_STATUS", "3");
        list.add(map);
        return  ResponseEntity.ok(JSONUtil.toJSon(list).toString());
    }



}
