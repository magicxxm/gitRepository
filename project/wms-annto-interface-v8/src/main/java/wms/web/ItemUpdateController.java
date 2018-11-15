package wms.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.service.Item;
import wms.business.dto.ItemCheckDTO;
import wms.common.crud.AccessDTO;
import wms.crud.dto.AnntoItemDTO;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
//@RequestMapping("/wms/robot")
public class ItemUpdateController {

    private final Item item;

    @Autowired
    public ItemUpdateController(Item item) {
        this.item = item;
    }

    //同步商品信息
    @RequestMapping(value = "/item/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> itemUpdate(@RequestBody AnntoItemDTO anntoItemDTO) {
        return ResponseEntity.ok(item.synchronous(anntoItemDTO));
    }

    //查询上游商品信息
    @RequestMapping(value = "/item/get",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnntoItemDTO> itemUpdate(@RequestBody ItemCheckDTO itemCheckDTO) {
        AnntoItemDTO anntoItemDTO = item.accept(itemCheckDTO);
        return ResponseEntity.ok().body(anntoItemDTO);
    }

}
