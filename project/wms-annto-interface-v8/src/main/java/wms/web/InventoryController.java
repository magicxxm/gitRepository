package wms.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.common.crud.AccessDTO;
import wms.service.Inventory;
import wms.service.StockUnitService;
import wms.common.crud.InventoryAccessDTO;
import wms.crud.dto.StockUnitCheckDTO;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
@RequestMapping("/wms/robot/inventory")
public class InventoryController {

    private final Inventory inventory;
    private final StockUnitService stockUnitService;

    @Autowired
    public InventoryController(Inventory inventory,StockUnitService stockUnitService) {
        this.inventory = inventory;
        this.stockUnitService = stockUnitService;
    }

    /*@RequestMapping(value = "/get",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryAccessDTO> getItemRecords(@RequestParam(required = false) String warehouseCode,
                                                             @RequestParam(required = false) String itemCode) {
        return ResponseEntity.ok(inventory.getInventory(warehouseCode,itemCode));
    }*/

    /*@RequestMapping(value = "/get",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryAccessDTO> getItemRecords(@RequestParam String warehouseCode,
                                                             @RequestParam String companyCode,
                                                             @RequestParam String itemCode,
                                                             @RequestParam  String inventoryType,
                                                             @RequestParam String page,
                                                             @RequestParam String pageNo) {
        return ResponseEntity.ok(stockUnitService.getStock(warehouseCode,companyCode,itemCode,inventoryType,page,pageNo));
    }*/

    @RequestMapping(value = "/get",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> getStockUnit(@RequestBody StockUnitCheckDTO stockUnitCheckDTO) {

        JSONObject jsonObject = JSONObject.fromObject(stockUnitCheckDTO);
        String data = jsonObject.toString();
        System.out.println("data === >" + data);
        return ResponseEntity.ok(stockUnitService.getStockUnit(stockUnitCheckDTO));
    }

    /*@RequestMapping(value = "/get",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryAccessDTO> getStockUnit(@RequestBody StockUnitCheckDTO stockUnitCheckDTO) {
        return ResponseEntity.ok(stockUnitService.acceptStockUnit(stockUnitCheckDTO));
    }*/
}
