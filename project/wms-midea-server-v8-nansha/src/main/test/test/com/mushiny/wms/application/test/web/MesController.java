package test.com.mushiny.wms.application.test.web;


import com.mushiny.wms.common.utils.JSONUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class MesController {


    /**
     * 物料同步接口
     * @Author: mingchun.mu@mushiy.com
     */
    @GetMapping(value = "/mItems2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> mItems(@RequestBody String param) {

        System.out.println(" 请求参数 ： " + param);

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
        return  ResponseEntity.ok(JSONUtil.toJSon(list).toString());
    }



}
