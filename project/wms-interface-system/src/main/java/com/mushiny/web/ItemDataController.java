package com.mushiny.web;

import com.mushiny.business.ItemBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.ItemService;
import com.mushiny.web.dto.ItemDTO;
import com.mushiny.web.dto.SkuNoDTO;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
@RestController
@RequestMapping("/wms/mushiny/")
public class ItemDataController {
    private final Logger log = LoggerFactory.getLogger(AdviceReceiptController.class);

    private final ItemService itemService;
    private final ItemBusiness itemBusiness;

    @Autowired
    public ItemDataController(ItemService itemService,
                              ItemBusiness itemBusiness){
        this.itemService = itemService;
        this.itemBusiness = itemBusiness;
    }

    //同步商品信息
    @RequestMapping(value = "/item/create",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> itemCreate(@RequestBody ItemDTO dto) {
        JSONObject jsonObject = JSONObject.fromObject(dto);
        log.info("接收到的信息是：==> " + jsonObject);
        List<String> itemIds = itemService.synchronous(dto);
        itemBusiness.updateItemSize(itemIds);
        return ResponseEntity.ok().build();
    }

    //同步商品条码信息
    @RequestMapping(value = "/item/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> itemUpdate(@RequestBody SkuNoDTO dto) {
        JSONObject jsonObject = JSONObject.fromObject(dto);
        log.info("接收到的信息是：itemNo = " + jsonObject);
        return ResponseEntity.ok(itemService.updateItem(dto));
    }

}
